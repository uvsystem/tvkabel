package com.unitedvision.tvkabel.core.service.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unitedvision.tvkabel.core.service.PelangganService;
import com.unitedvision.tvkabel.core.service.PerusahaanService;
import com.unitedvision.tvkabel.core.validator.Validator;
import com.unitedvision.tvkabel.exception.ApplicationException;
import com.unitedvision.tvkabel.exception.DataDuplicationException;
import com.unitedvision.tvkabel.exception.EntityNotExistException;
import com.unitedvision.tvkabel.persistence.entity.Alamat;
import com.unitedvision.tvkabel.persistence.entity.Operator;
import com.unitedvision.tvkabel.persistence.entity.Pegawai;
import com.unitedvision.tvkabel.persistence.entity.Pelanggan;
import com.unitedvision.tvkabel.persistence.entity.Perusahaan;
import com.unitedvision.tvkabel.persistence.entity.Pegawai.Kredensi;
import com.unitedvision.tvkabel.persistence.entity.Pegawai.Role;
import com.unitedvision.tvkabel.persistence.entity.Pegawai.Status;
import com.unitedvision.tvkabel.persistence.repository.PegawaiRepository;
import com.unitedvision.tvkabel.persistence.repository.PelangganRepository;
import com.unitedvision.tvkabel.persistence.repository.PembayaranRepository;
import com.unitedvision.tvkabel.persistence.repository.PerusahaanRepository;
import com.unitedvision.tvkabel.util.DateUtil;

@Service
@Transactional(readOnly = true)
public class PerusahaanServiceImpl implements PerusahaanService {
	@Autowired
	private PelangganService pelangganService;
	
	@Autowired
	private PerusahaanRepository perusahaanRepository;
	@Autowired
	private PegawaiRepository pegawaiRepository;
	@Autowired
	private PembayaranRepository pembayaranRepository;
	@Autowired
	private PelangganRepository pelangganRepository;
	@Autowired
	private Validator validator;

	@Override
	@Transactional(readOnly = false)
	public Perusahaan save(Perusahaan domain) throws DataDuplicationException {
		domain = validator.validate(domain);

		try {
			return perusahaanRepository.save(domain);
		} catch(PersistenceException e) {
			throw new DataDuplicationException(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Perusahaan domain) {
		domain = perusahaanRepository.findOne(domain.getId());
		perusahaanRepository.delete(domain);
	}

	@Override
	public Perusahaan getOne(int id) throws EntityNotExistException {
		return perusahaanRepository.findOne(id);
	}

	@Override
	public Perusahaan getByKode(String kode) throws EntityNotExistException {
		return perusahaanRepository.findByKode(kode);
	}

	@Override
	public List<Perusahaan> getAll() throws EntityNotExistException {
		return perusahaanRepository.findAll();
	}

	@Override
	public void setMapLocation(Perusahaan perusahaan, float latitude, float longitude) throws ApplicationException {
		Alamat alamat = perusahaan.getAlamat();
		alamat.setLatitude(latitude);
		alamat.setLongitude(longitude);
		
		perusahaan.setAlamat(alamat);
		
		save(perusahaan);
	}

	@Override
	@Transactional(readOnly = false)
	public Operator regist(Perusahaan perusahaan) throws ApplicationException {
		Perusahaan perusahaanEntity = perusahaan;
		perusahaanEntity.generateKode(getAvailableId());
		perusahaan = save(perusahaanEntity);
		
		final Kredensi kredensi = new Kredensi(perusahaan.getEmail(), "admin", Role.OWNER);
		Pegawai pegawai = new Pegawai(0, "new", perusahaan, "OWNER", kredensi, Status.AKTIF);
		pegawai.generateKode(pegawaiRepository.countByPerusahaan(perusahaan));
		pegawai = pegawaiRepository.save(pegawai);
		
		return pegawai.toOperator();
	}
	
	private int getAvailableId() {
		Perusahaan perusahaanEntity = perusahaanRepository.findFirstByOrderByIdDesc();
		
		return perusahaanEntity.getId() + 1;
	}
	
	@Override
	public long countTagihanBulanBerjalan(Perusahaan perusahaan) {
		Date tanggalAwal = DateUtil.getFirstDate();
		Date tanggalAkhir = DateUtil.getLastDate();
		
		return countTagihanBulanBerjalan(perusahaan, tanggalAwal, tanggalAkhir);
	}
	
	@Override
	public long countTagihanBulanBerjalan(Perusahaan perusahaan, Date tanggalAwal, Date tanggalAkhir) {
		long totalPembayaran = pembayaranRepository.countByPegawai_PerusahaanAndTanggalBayarBetween(perusahaan, tanggalAwal, tanggalAkhir);
		long totalPelangganBerhenti = pelangganRepository.countByPerusahaanAndStatus(perusahaan, Pelanggan.Status.BERHENTI);
		long totalPelangganPutus = pelangganRepository.countByPerusahaanAndStatus(perusahaan, Pelanggan.Status.PUTUS);

		return (totalPembayaran * perusahaan.getIuran()) + ((totalPelangganPutus + totalPelangganBerhenti) * (perusahaan.getIuran() / 2));
	}

	@Override
	public long countEstimasiPemasukanBulanan(Perusahaan perusahaan) {
		return pelangganRepository.sumarizeEstimasiPemasukanBulanan(perusahaan, Pelanggan.Status.AKTIF);
	}
	
	@Override
	public long countEstimasiTagihanBulanan(Perusahaan perusahaan) {
		long jumlahPelangganAktif = pelangganService.count(perusahaan, Pelanggan.Status.AKTIF);
		long jumlahPelangganBerhenti = pelangganService.count(perusahaan, Pelanggan.Status.BERHENTI);
		long jumlahPelangganPutus = pelangganService.count(perusahaan, Pelanggan.Status.PUTUS);

		long estimasiTagihanBulanan = (jumlahPelangganAktif * perusahaan.getIuran()) +
				((jumlahPelangganBerhenti + jumlahPelangganPutus) * (perusahaan.getIuran() / 2));

		return estimasiTagihanBulanan;
	}
	
	@Override
	public long countTotalAkumulasiTunggakan(Perusahaan perusahaan) {
		return pelangganRepository.summarizeTotalAkumulasiTunggakan(perusahaan, Pelanggan.Status.AKTIF);
	}
	
	@Override
	public long countPemasukanBulanBerjalan(Perusahaan perusahaan) {
		Date tanggalAwal = DateUtil.getFirstDate();
		Date tanggalAkhir = DateUtil.getLastDate();
		
		return pembayaranRepository.countPemasukanBulanBerjalan(perusahaan, tanggalAwal, tanggalAkhir);
	}
}
