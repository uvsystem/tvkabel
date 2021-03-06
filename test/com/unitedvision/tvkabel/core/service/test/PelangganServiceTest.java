package com.unitedvision.tvkabel.core.service.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.unitedvision.tvkabel.core.service.KelurahanService;
import com.unitedvision.tvkabel.core.service.PegawaiService;
import com.unitedvision.tvkabel.core.service.PelangganService;
import com.unitedvision.tvkabel.core.service.PembayaranService;
import com.unitedvision.tvkabel.core.service.PerusahaanService;
import com.unitedvision.tvkabel.core.validator.Validator;
import com.unitedvision.tvkabel.exception.ApplicationException;
import com.unitedvision.tvkabel.exception.DataDuplicationException;
import com.unitedvision.tvkabel.exception.EmptyCodeException;
import com.unitedvision.tvkabel.exception.EmptyIdException;
import com.unitedvision.tvkabel.exception.EntityNotExistException;
import com.unitedvision.tvkabel.exception.StatusChangeException;
import com.unitedvision.tvkabel.persistence.SpringDataJpaConfig;
import com.unitedvision.tvkabel.persistence.entity.Alamat;
import com.unitedvision.tvkabel.persistence.entity.Kelurahan;
import com.unitedvision.tvkabel.persistence.entity.Kontak;
import com.unitedvision.tvkabel.persistence.entity.Pegawai;
import com.unitedvision.tvkabel.persistence.entity.Pelanggan;
import com.unitedvision.tvkabel.persistence.entity.Pembayaran;
import com.unitedvision.tvkabel.persistence.entity.Perusahaan;
import com.unitedvision.tvkabel.persistence.entity.Pelanggan.Detail;
import com.unitedvision.tvkabel.persistence.entity.Pelanggan.Status;
import com.unitedvision.tvkabel.persistence.repository.PelangganRepository;
import com.unitedvision.tvkabel.util.CodeUtil;
import com.unitedvision.tvkabel.util.DateUtil;
import com.unitedvision.tvkabel.util.CodeUtil.CodeGenerator;

@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = {SpringDataJpaConfig.class})
@Transactional
@TransactionConfiguration (defaultRollback = true)
public class PelangganServiceTest {
	@Autowired
	private PelangganService pelangganService;
	@Autowired
	private PegawaiService pegawaiService;
	@Autowired
	private PembayaranService pembayaranService;
	@Autowired
	private KelurahanService kelurahanService;
	@Autowired
	private PerusahaanService perusahaanService;

	@Autowired
	private PelangganRepository pelangganRepo;
	
	@Autowired
	private Validator validator;
	
	@Test
	public void insertPelanggan_Success() throws ApplicationException {
		Perusahaan perusahaan = perusahaanService.getByKode("COM1");
		Kelurahan kelurahan = kelurahanService.getOne(22);
		Alamat alamat = new Alamat(1, "Detail", 0, 0);
		Kontak kontak = new Kontak("823586", "081377653421", "email@gmail.com");
		Detail detail = new Detail(new Date(), 1, 50000, 0);

		Pelanggan newPelanggan = new Pelanggan(0, "1", perusahaan, "PLGT", "Pelanggan Test", "Pengamen",
				kelurahan, alamat, kontak, detail, Status.AKTIF);
		
		Pelanggan pelanggan = newPelanggan;
		Pelanggan saved = pelangganService.save(pelanggan);
		
		assertNotNull(saved);

		//Check Tanggal Mulai
		Date date = saved.getTanggalMulai();
		Date now = DateUtil.getSimpleNow();
		assertEquals(DateUtil.getYear(now), DateUtil.getYear(date));
		assertEquals(DateUtil.getMonth(now), DateUtil.getMonth(date));
		assertEquals(DateUtil.getDay(now), DateUtil.getDay(date));
	}

	@Test
	public void insertPelanggan_KodeException() throws EntityNotExistException, EmptyIdException, EmptyCodeException {
		Perusahaan perusahaan = perusahaanService.getByKode("COM1");
		Kelurahan kelurahan = kelurahanService.getOne(22);
		Alamat alamat = new Alamat(1, "Detail", 0, 0);
		Kontak kontak = new Kontak("823586", "081377653421", "email@gmail.com");
		Detail detail = new Detail(new Date(), 1, 50000, 0);

		Pelanggan newPelanggan = new Pelanggan(0, "XXX", perusahaan, "WS05001", "Belum Ada", "Pengamen",
				kelurahan, alamat, kontak, detail, Status.AKTIF);

		try {
			pelangganService.save(newPelanggan);
		} catch (ApplicationException e) {
			String message = e.getMessage();
			assertEquals("Kode yang anda masukkan sudah digunakan.", message);
		}
	}

	@Test
	public void insertPelanggan_NomorBukuException() throws EntityNotExistException, EmptyIdException, EmptyCodeException {
		Perusahaan perusahaan = perusahaanService.getByKode("COM1");
		Kelurahan kelurahan = kelurahanService.getOne(22);
		Alamat alamat = new Alamat(1, "Detail", 0, 0);
		Kontak kontak = new Kontak("823586", "081377653421", "email@gmail.com");
		Detail detail = new Detail(new Date(), 1, 50000, 0);

		Pelanggan newPelanggan = new Pelanggan(0, "---", perusahaan, "XXX", "Belum Ada", "Pengamen",
				kelurahan, alamat, kontak, detail, Status.AKTIF);
		
		try {
			pelangganService.save(newPelanggan);
		} catch (ApplicationException e) {
			String message = e.getMessage();
			assertEquals("Nomor Buku yang anda masukkan sudah digunakan.", message);
		}
	}

	@Test
	public void insertPelanggan_NamaException() throws EntityNotExistException, EmptyIdException, EmptyCodeException {
		Perusahaan perusahaan = perusahaanService.getByKode("COM1");
		Kelurahan kelurahan = kelurahanService.getOne(22);
		Alamat alamat = new Alamat(1, "Detail", 0, 0);
		Kontak kontak = new Kontak("823586", "081377653421", "email@gmail.com");
		Detail detail = new Detail(new Date(), 1, 50000, 0);

		Pelanggan newPelanggan = new Pelanggan(0, "-------", perusahaan, "---", "Kel. Walewangko-Mandagi", "Pengamen",
				kelurahan, alamat, kontak, detail, Status.AKTIF);
		
		try {
			pelangganService.save(newPelanggan);
		} catch (ApplicationException e) {
			String message = e.getMessage();
			assertEquals("Nama yang anda masukkan sudah digunakan.", message);
		}
	}

	@Test
	@Ignore
	public void updatePelanggan_Success() throws ApplicationException { }

	@Test
	public void updatePelanggan_KodeException() throws EntityNotExistException, EmptyIdException, EmptyCodeException {
		Pelanggan pelanggan = pelangganService.getOne(35);
		pelanggan.setKode("WS02018");
		
		try {
			pelangganService.save(pelanggan);
		} catch (ApplicationException e) {
			String message = e.getMessage();
			assertEquals("Kode yang anda masukkan sudah digunakan.", message);
		}
	}

	@Test
	public void updatePelanggan_NomorBukuException() throws EntityNotExistException, EmptyIdException, EmptyCodeException {
		Pelanggan pelanggan = pelangganService.getOne(35);
		pelanggan.setNomorBuku("17");
		
		try {
			pelangganService.save(pelanggan);
		} catch (ApplicationException e) {
			String message = e.getMessage();
			assertEquals("Nomor Buku yang anda masukkan sudah digunakan.", message);
		}
	}

	@Test
	public void updatePelanggan_NamaException() throws EntityNotExistException, EmptyIdException, EmptyCodeException {
		Pelanggan pelanggan = pelangganService.getOne(35);
		pelanggan.setNama("Engelbert Koagow");
		
		try {
			pelangganService.save(pelanggan);
		} catch (ApplicationException e) {
			String message = e.getMessage();
			assertEquals("Nama yang anda masukkan sudah digunakan.", message);
		}
	}

	@Test
	public void bannedWorks() throws ApplicationException {
		Perusahaan perusahaan = perusahaanService.getOne(17);
		Kelurahan kelurahan = kelurahanService.getOne(22);
		Alamat alamat = new Alamat(1, "Detail", 0, 0);
		Kontak kontak = new Kontak("823586", "081377653421", "email@gmail.com");
		Detail detail = new Detail(DateUtil.getDate("12/1/2014"), 1, 50000, 0);

		Pelanggan newPelanggan = new Pelanggan(0, "1", perusahaan, "PLGT", "Pelanggan Test", "Pengamen",
				kelurahan, alamat, kontak, detail, Status.AKTIF);
		
		Pelanggan saved = pelangganService.save(newPelanggan);
		assertNotNull(saved);

		//BANNED PROCESS
		Pelanggan pelanggan = pelangganService.getOne(saved.getId());
		pelanggan.setStatus(Status.AKTIF);
		
		pelangganService.banned(pelanggan);

		Pelanggan pelangganBanned = pelangganService.getOne(pelanggan.getId());

		Assert.assertEquals(Status.PUTUS, pelanggan.getStatus());
		Assert.assertEquals(2, pelangganBanned.getTunggakan());
	}
	
	@Test(expected = StatusChangeException.class)
	public void banBannedPelanggan() throws ApplicationException {
		Perusahaan perusahaan = perusahaanService.getByKode("COM1");
		Kelurahan kelurahan = kelurahanService.getOne(22);
		Alamat alamat = new Alamat(1, "Detail", 0, 0);
		Kontak kontak = new Kontak("823586", "081377653421", "email@gmail.com");
		Detail detail = new Detail(DateUtil.getDate("12/1/2014"), 1, 50000, 0);

		Pelanggan newPelanggan = new Pelanggan(0, "1", perusahaan, "PLGT", "Pelanggan Test", "Pengamen",
				kelurahan, alamat, kontak, detail, Status.AKTIF);
		
		Pelanggan saved = pelangganService.save(newPelanggan);
		
		assertNotNull(saved);

		//BANNED PROCESS
		Pelanggan pelanggan = pelangganService.getOne(saved.getId());
		pelanggan.setStatus(Status.AKTIF);
		
		pelangganService.banned(pelanggan);

		Pelanggan pelangganBanned = pelangganService.getOne(pelanggan.getId());

		Assert.assertEquals(Status.PUTUS, pelanggan.getStatus());
		Assert.assertEquals(1, pelangganBanned.getTunggakan());

		//REBEND PROCESS
		pelanggan.setStatus(Status.PUTUS);
		
		pelangganService.banned(pelanggan);
	}
	
	@Test
	public void passivateWorks() throws ApplicationException {
		Perusahaan perusahaan = perusahaanService.getByKode("COM1");
		Kelurahan kelurahan = kelurahanService.getOne(22);
		Alamat alamat = new Alamat(1, "Detail", 0, 0);
		Kontak kontak = new Kontak("823586", "081377653421", "email@gmail.com");
		Detail detail = new Detail(new Date(), 1, 50000, 0);

		Pelanggan newPelanggan = new Pelanggan(0, "1", perusahaan, "PLGT", "Pelanggan Test", "Pengamen",
				kelurahan, alamat, kontak, detail, Status.AKTIF);
		
		Pelanggan saved = pelangganService.save(newPelanggan);
		
		assertNotNull(saved);

		//PASSIVATE PROCESS
		Pelanggan pelanggan = pelangganService.getOne(saved.getId());
		pelanggan.setStatus(Status.AKTIF);
		
		pelangganService.passivate(pelanggan);

		Pelanggan pelangganPassive = pelangganService.getOne(pelanggan.getId());

		Assert.assertEquals(Status.BERHENTI, pelanggan.getStatus());
		Assert.assertEquals(pelanggan.getTunggakan(), pelangganPassive.getTunggakan());
	}

	@Test(expected = StatusChangeException.class)
	public void passivatePassivePelanggan() throws ApplicationException {
		Perusahaan perusahaan = perusahaanService.getByKode("COM1");
		Kelurahan kelurahan = kelurahanService.getOne(22);
		Alamat alamat = new Alamat(1, "Detail", 0, 0);
		Kontak kontak = new Kontak("823586", "081377653421", "email@gmail.com");
		Detail detail = new Detail(new Date(), 1, 50000, 0);

		Pelanggan newPelanggan = new Pelanggan(0, "1", perusahaan, "PLGT", "Pelanggan Test", "Pengamen",
				kelurahan, alamat, kontak, detail, Status.AKTIF);
		
		Pelanggan saved = pelangganService.save(newPelanggan);
		
		assertNotNull(saved);

		//PASSIVATE PROCESS
		Pelanggan pelanggan = pelangganService.getOne(saved.getId());
		pelanggan.setStatus(Status.AKTIF);
		
		pelangganService.passivate(pelanggan);

		Pelanggan pelangganPassive = pelangganService.getOne(pelanggan.getId());

		Assert.assertEquals(Status.BERHENTI, pelanggan.getStatus());
		Assert.assertEquals(pelanggan.getTunggakan(), pelangganPassive.getTunggakan());

		//REPASSIVATE PROCESS
		Pelanggan repassivatePelanggan = pelangganService.getOne(pelangganPassive.getId());
		repassivatePelanggan.setStatus(Status.BERHENTI);
		
		pelangganService.passivate(repassivatePelanggan);
	}

	@Test
	public void activateWorks() throws ApplicationException {
		Perusahaan perusahaan = perusahaanService.getByKode("COM1");
		Kelurahan kelurahan = kelurahanService.getOne(22);
		Alamat alamat = new Alamat(1, "Detail", 0, 0);
		Kontak kontak = new Kontak("823586", "081377653421", "email@gmail.com");
		Detail detail = new Detail(new Date(), 1, 50000, 0);

		Pelanggan newPelanggan = new Pelanggan(0, "1", perusahaan, "PLGT", "Pelanggan Test", "Pengamen",
				kelurahan, alamat, kontak, detail, Status.AKTIF);
		
		Pelanggan saved = pelangganService.save(newPelanggan);
		
		assertNotNull(saved);

		//ACTIVATE PROCESS
		Pelanggan pelanggan = pelangganService.getOne(saved.getId());
		pelanggan.setStatus(Status.BERHENTI);
		
		pelangganService.activate(pelanggan);

		Pelanggan pelangganActive = pelangganService.getOne(pelanggan.getId());

		Assert.assertEquals(Status.AKTIF, pelanggan.getStatus());
		Assert.assertEquals(0, pelangganActive.getTunggakan());
		Assert.assertEquals(DateUtil.getSimpleNow().getTime(), DateUtil.getSimpleDate(pelangganActive.getTanggalMulai()).getTime());
	}
	
	@Test(expected = StatusChangeException.class)
	public void activateActivePelanggan() throws ApplicationException {
		Perusahaan perusahaan = perusahaanService.getByKode("COM1");
		Kelurahan kelurahan = kelurahanService.getOne(22);
		Alamat alamat = new Alamat(1, "Detail", 0, 0);
		Kontak kontak = new Kontak("823586", "081377653421", "email@gmail.com");
		Detail detail = new Detail(new Date(), 1, 50000, 0);

		Pelanggan newPelanggan = new Pelanggan(0, "1", perusahaan, "PLGT", "Pelanggan Test", "Pengamen",
				kelurahan, alamat, kontak, detail, Status.AKTIF);
		
		Pelanggan saved = pelangganService.save(newPelanggan);
		
		assertNotNull(saved);

		//ACTIVATE PROCESS
		Pelanggan pelanggan = pelangganService.getOne(saved.getId());
		pelanggan.setStatus(Status.BERHENTI);
		
		pelangganService.activate(pelanggan);

		Pelanggan pelangganActive = pelangganService.getOne(pelanggan.getId());

		Assert.assertEquals(Status.AKTIF, pelanggan.getStatus());
		Assert.assertEquals(0, pelangganActive.getTunggakan());
		Assert.assertEquals(DateUtil.getSimpleNow().getTime(), DateUtil.getSimpleDate(pelangganActive.getTanggalMulai()).getTime());

		//REACTIVATE PROCESS
		Pelanggan reactivatePelanggan = pelangganService.getOne(pelangganActive.getId());
		reactivatePelanggan.setStatus(Status.AKTIF);
		
		pelangganService.activate(reactivatePelanggan);
	}

	@Test
	@Ignore
	public void recountTunggakanWorks() throws ApplicationException {
		pelangganService.recountTunggakan();
		
		Pelanggan pelanggan35 = pelangganService.getOne(35); //Pelanggan Aktif
		Assert.assertEquals(3, pelanggan35.getTunggakan());
		
		Pelanggan pelanggan842 = pelangganService.getOne(842); //Pelanggan Removed
		Assert.assertEquals(9, pelanggan842.getTunggakan());

		Pelanggan pelanggan793 = pelangganService.getOne(793); //Pelanggan Putus
		Assert.assertEquals(4, pelanggan793.getTunggakan());
	}
	
	@Test
	public void testRecountTunggakan() throws EntityNotExistException, DataDuplicationException {
		Pelanggan pelanggan = pelangganService.getOne(35);
		
		pelangganService.recountTunggakan(pelanggan);
		
		Pelanggan pelangganUpdated = pelangganService.getOne(35);
		
		assertEquals(0, pelangganUpdated.getTunggakan());
	}
	
	@Test
	public void testGetByPerusahaanAndKodeAndStatus() throws EntityNotExistException {
		Perusahaan perusahaan = perusahaanService.getOne(17);
		String kode = "WS01";
		Status status = Status.AKTIF;

		List<? extends Pelanggan> list = pelangganService.getByKode(perusahaan, status, kode, 0);
		
		assertNotEquals(0, list.size());
	}
	
	@Test
	@Ignore
	public void testResetKode() throws EntityNotExistException {
		Perusahaan perusahaan = perusahaanService.getOne(17);
		Kelurahan kelurahan = kelurahanService.getOne(22);
		
		pelangganService.resetKode(perusahaan, kelurahan, 1);

		Pelanggan pelanggan = pelangganService.getOne(70);
		assertEquals("WS01001", pelanggan.getKode());

		Pelanggan pelanggan2 = pelangganService.getOne(74);
		assertEquals("WS01002", pelanggan2.getKode());

		Pelanggan pelanggan3 = pelangganService.getOne(95);
		assertEquals("WS01003", pelanggan3.getKode());

		Pelanggan pelanggan4 = pelangganService.getOne(110);
		assertEquals("WS01004", pelanggan4.getKode());

		Pelanggan pelanggan5 = pelangganService.getOne(162);
		assertEquals("WS01005", pelanggan5.getKode());

		Pelanggan pelanggan6 = pelangganService.getOne(214);
		assertEquals("WS01010", pelanggan6.getKode());
	}
	
	@Test
	public void testResetKode_Pelanggan() throws EntityNotExistException, EmptyCodeException {
		CodeUtil.CodeGenerator codeGenerator = new CodeGenerator();

		Pelanggan pelanggan = pelangganService.getOne(37);
		String generatedKode = codeGenerator.createKode(pelanggan);
		assertNotEquals(generatedKode, pelanggan.getKode());
		
		pelanggan.setKode(generatedKode);
		Pelanggan pelangganUpdated = pelangganService.getOne(37);
		assertEquals(generatedKode, pelangganUpdated.getKode());
	}
	
	@Test (expected = EntityNotExistException.class)
	public void testGetOne() throws EntityNotExistException {
		Pelanggan pelanggan = pelangganService.getOne(0);
		
		assertNotNull(pelanggan);
	}
	
	@Test (expected = EntityNotExistException.class)
	public void testGet() throws EntityNotExistException {
		Perusahaan perusahaan = perusahaanService.getOne(17);
		List<Pelanggan> listPelanggan = pelangganService.get(perusahaan, Status.REMOVED);
		
		assertNotNull(listPelanggan);
		assertNotEquals(0, listPelanggan.size());
	}
	
	@Test
	public void testGetByTanggal_Like() throws EntityNotExistException {
		String tanggal = "22";
		tanggal = DateUtil.getDayString(tanggal);
		
		List<Pelanggan> listPelanggan = pelangganService.get(Status.AKTIF, tanggal);
		
		assertNotNull(listPelanggan);
		assertNotEquals(0, listPelanggan.size());
		assertEquals(1, listPelanggan.size());
	}
	
	@Test
	public void testRecountTunggakan_WithTanggal() throws ApplicationException {
		String tanggal = "1";
		
		pelangganService.recountTunggakan(tanggal);

		Pegawai pegawai = pegawaiService.getOne(14);
		Date now = DateUtil.getNow();
		List<Pembayaran> listPembayaran = pembayaranService.get(pegawai, now, now, 1);
		
		assertNotNull(listPembayaran);
		assertEquals(0, listPembayaran.size());
	}
}
