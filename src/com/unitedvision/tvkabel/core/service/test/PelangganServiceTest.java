package com.unitedvision.tvkabel.core.service.test;

import static org.junit.Assert.*;

import java.time.Month;
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

import com.unitedvision.tvkabel.core.domain.Pelanggan.Status;
import com.unitedvision.tvkabel.core.domain.Pelanggan;
import com.unitedvision.tvkabel.core.domain.Perusahaan;
import com.unitedvision.tvkabel.core.service.KelurahanService;
import com.unitedvision.tvkabel.core.service.PelangganService;
import com.unitedvision.tvkabel.core.service.PembayaranService;
import com.unitedvision.tvkabel.core.service.PerusahaanService;
import com.unitedvision.tvkabel.core.validator.Validator;
import com.unitedvision.tvkabel.exception.ApplicationException;
import com.unitedvision.tvkabel.exception.EntityNotExistException;
import com.unitedvision.tvkabel.exception.StatusChangeException;
import com.unitedvision.tvkabel.persistence.SpringDataJpaConfig;
import com.unitedvision.tvkabel.persistence.entity.PelangganEntity;
import com.unitedvision.tvkabel.persistence.repository.PelangganRepository;
import com.unitedvision.tvkabel.util.DateUtil;
import com.unitedvision.tvkabel.web.model.PelangganModel;

@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = {SpringDataJpaConfig.class})
@Transactional
@TransactionConfiguration (defaultRollback = true)
public class PelangganServiceTest {
	@Autowired
	private PelangganService pelangganService;
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
	public void insertPelangganSuccess() throws ApplicationException {
		Perusahaan perusahaan = perusahaanService.getByKode("COM1");

		PelangganModel model = new PelangganModel(0, "PLGT", "Pelanggan Test", "Pengamen", perusahaan.toEntity(), 
				"Manado", "Mapanget", "Paniki Bawah", 6, "", 
				"", "", "", 
				"5/1/2014", 1, 30000, 0, Status.AKTIF);
		
		PelangganEntity PelangganEntity = model.toEntity();
		Pelanggan saved = pelangganService.save(PelangganEntity);
		
		assertNotNull(saved);

		//Check Tanggal Mulai
		Date date = saved.getTanggalMulai();
		assertEquals(2014, DateUtil.getYear(date));
		assertEquals(Month.MAY, DateUtil.getMonth(date));
		assertEquals(1, DateUtil.getDay(date));
	}

	@Test
	public void bannedWorks() throws ApplicationException {
		Perusahaan perusahaan = perusahaanService.getByKode("COM1");

		PelangganModel model = new PelangganModel(0, "PLGT", "Pelanggan Test", "Pengamen", perusahaan.toEntity(), 
				"Manado", "Mapanget", "Paniki Bawah", 6, "", 
				"", "", "", 
				"5/1/2014", 1, 30000, 0, Status.AKTIF);
		
		PelangganEntity PelangganEntity = model.toEntity();
		Pelanggan saved = pelangganService.save(PelangganEntity);
		
		assertNotNull(saved);

		//BANNED PROCESS
		Pelanggan pelanggan = pelangganService.getOne(saved.getId());
		pelanggan.setStatus(Status.AKTIF);
		
		pelangganService.banned(pelanggan);

		Pelanggan pelangganBanned = pelangganService.getOne(pelanggan.getId());

		Assert.assertEquals(Status.PUTUS, pelanggan.getStatus());
		Assert.assertEquals(5, pelangganBanned.getTunggakan());
	}
	
	@Test(expected = StatusChangeException.class)
	public void banBannedPelanggan() throws ApplicationException {
		Perusahaan perusahaan = perusahaanService.getByKode("COM1");

		PelangganModel model = new PelangganModel(0, "PLGT", "Pelanggan Test", "Pengamen", perusahaan.toEntity(), 
				"Manado", "Mapanget", "Paniki Bawah", 6, "", 
				"", "", "", 
				"5/1/2014", 1, 30000, 0, Status.AKTIF);
		
		PelangganEntity PelangganEntity = model.toEntity();
		Pelanggan saved = pelangganService.save(PelangganEntity);
		
		assertNotNull(saved);

		//BANNED PROCESS
		Pelanggan pelanggan = pelangganService.getOne(saved.getId());
		pelanggan.setStatus(Status.AKTIF);
		
		pelangganService.banned(pelanggan);

		Pelanggan pelangganBanned = pelangganService.getOne(pelanggan.getId());

		Assert.assertEquals(Status.PUTUS, pelanggan.getStatus());
		Assert.assertEquals(5, pelangganBanned.getTunggakan());

		//REBEND PROCESS
		pelanggan.setStatus(Status.PUTUS);
		
		pelangganService.banned(pelanggan);
	}
	
	@Test
	public void passivateWorks() throws ApplicationException {
		Perusahaan perusahaan = perusahaanService.getByKode("COM1");

		PelangganModel model = new PelangganModel(0, "PLGT", "Pelanggan Test", "Pengamen", perusahaan.toEntity(), 
				"Manado", "Mapanget", "Paniki Bawah", 6, "", 
				"", "", "", 
				"5/1/2014", 1, 30000, 0, Status.AKTIF);
		
		PelangganEntity PelangganEntity = model.toEntity();
		Pelanggan saved = pelangganService.save(PelangganEntity);
		
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

		PelangganModel model = new PelangganModel(0, "PLGT", "Pelanggan Test", "Pengamen", perusahaan.toEntity(), 
				"Manado", "Mapanget", "Paniki Bawah", 6, "", 
				"", "", "", 
				"5/1/2014", 1, 30000, 0, Status.AKTIF);
		
		PelangganEntity PelangganEntity = model.toEntity();
		Pelanggan saved = pelangganService.save(PelangganEntity);
		
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

		PelangganModel model = new PelangganModel(0, "PLGT", "Pelanggan Test", "Pengamen", perusahaan.toEntity(), 
				"Manado", "Mapanget", "Paniki Bawah", 6, "", 
				"", "", "", 
				"5/1/2014", 1, 30000, 0, Status.AKTIF);
		
		PelangganEntity PelangganEntity = model.toEntity();
		Pelanggan saved = pelangganService.save(PelangganEntity);
		
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

		PelangganModel model = new PelangganModel(0, "PLGT", "Pelanggan Test", "Pengamen", perusahaan.toEntity(), 
				"Manado", "Mapanget", "Paniki Bawah", 6, "", 
				"", "", "", 
				"5/1/2014", 1, 30000, 0, Status.AKTIF);
		
		PelangganEntity PelangganEntity = model.toEntity();
		Pelanggan saved = pelangganService.save(PelangganEntity);
		
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
	public void testGetByPerusahaanAndKodeAndStatus() throws EntityNotExistException {
		Perusahaan perusahaan = perusahaanService.getOne(17);
		String kode = "PLG000";
		Status status = Status.AKTIF;

		List<? extends Pelanggan> list = pelangganService.getByKode(perusahaan, status, kode, 0);
		
		assertNotEquals(0, list.size());
	}
	
}
