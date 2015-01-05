package com.unitedvision.tvkabel.core.service.test;

import static org.junit.Assert.*;

import java.time.Month;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.unitedvision.tvkabel.core.domain.Pembayaran;
import com.unitedvision.tvkabel.core.domain.Perusahaan;
import com.unitedvision.tvkabel.core.service.PegawaiService;
import com.unitedvision.tvkabel.core.service.PelangganService;
import com.unitedvision.tvkabel.core.service.PembayaranService;
import com.unitedvision.tvkabel.core.service.PerusahaanService;
import com.unitedvision.tvkabel.exception.DataDuplicationException;
import com.unitedvision.tvkabel.exception.EntityNotExistException;
import com.unitedvision.tvkabel.exception.NotPayableCustomerException;
import com.unitedvision.tvkabel.exception.UncompatibleTypeException;
import com.unitedvision.tvkabel.exception.UnpaidBillException;
import com.unitedvision.tvkabel.persistence.SpringDataJpaConfig;
import com.unitedvision.tvkabel.persistence.entity.PegawaiEntity;
import com.unitedvision.tvkabel.persistence.entity.PelangganEntity;
import com.unitedvision.tvkabel.persistence.entity.PembayaranEntity;
import com.unitedvision.tvkabel.persistence.entity.PembayaranEntity.TagihanValue;
import com.unitedvision.tvkabel.util.DateUtil;

@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = {SpringDataJpaConfig.class})
@Transactional
@TransactionConfiguration (defaultRollback = true)
public class PembayaranServiceTest {
	@Autowired
	private PembayaranService pembayaranService;
	@Autowired
	private PerusahaanService perusahaanService;
	@Autowired
	private PelangganService pelangganService;
	@Autowired
	private PegawaiService pegawaiService;
	
	@Test
	public void testGet() throws EntityNotExistException {
		Perusahaan perusahaan = perusahaanService.getOne(17);
		Date tanggalMulai = DateUtil.getFirstDate();
		Date tanggalAkhir = DateUtil.getLastDate();
		int lastNumber = 0;
		
		@SuppressWarnings("unchecked")
		List<PembayaranEntity> list = (List<PembayaranEntity>)pembayaranService.get(perusahaan, tanggalMulai, tanggalAkhir, lastNumber);
		
		for (Pembayaran pembayaran : list) {
			assertNotNull(pembayaran.getId());
			assertNotEquals(0, pembayaran.getId());
		}
	}
	
	@Test
	public void testPay() throws EntityNotExistException, NotPayableCustomerException, UnpaidBillException, UncompatibleTypeException, DataDuplicationException {
		Date tanggalBayar = DateUtil.getSimpleNow();
		PelangganEntity pelangganEntity = pelangganService.getOne(35).toEntity();
		PegawaiEntity pegawaiEntity = pegawaiService.getOne(15).toEntity();
		long jumlahBayar = pelangganEntity.getIuran();
		TagihanValue tagihanValue = new TagihanValue(2014, Month.OCTOBER);

		Pembayaran pembayaran = new PembayaranEntity(tanggalBayar, pelangganEntity, pegawaiEntity, jumlahBayar, tagihanValue);
		assertEquals(1, pelangganEntity.getTunggakan());
		
		pembayaranService.pay(pembayaran);
		PelangganEntity pelangganEntityUpdated = pelangganService.getOne(35).toEntity();
		assertEquals(3, pelangganEntityUpdated.getTunggakan());
	}
	
	@Test
	public void testDelete() throws EntityNotExistException, NotPayableCustomerException, UnpaidBillException, UncompatibleTypeException, DataDuplicationException {
		PelangganEntity pelangganEntity = pelangganService.getOne(35).toEntity();

		Pembayaran pembayaranTerakhir = pembayaranService.getLast(pelangganEntity);
		assertEquals(2014, pembayaranTerakhir.getTahun());
		assertEquals(Month.SEPTEMBER, pembayaranTerakhir.getBulan());
		
		assertEquals(1, pelangganEntity.getTunggakan());
		pembayaranService.delete(pembayaranTerakhir);

		PelangganEntity pelangganEntityDeleted = pelangganService.getOne(35).toEntity();
		assertEquals(5, pelangganEntityDeleted.getTunggakan());
	}
	
	@Test
	public void testGetPayableTagihan() throws EntityNotExistException {
		PelangganEntity pelangganEntity = pelangganService.getOne(35).toEntity();
		TagihanValue tagihanValue = pembayaranService.getPayableTagihan(pelangganEntity).toEntity();
		
		assertEquals(Month.OCTOBER, tagihanValue.getBulan());
		assertEquals(2014, tagihanValue.getTahun());
	}
	
	@Test
	public void testGetLast() throws EntityNotExistException {
		PelangganEntity pelangganEntity = pelangganService.getOne(35).toEntity();

		Pembayaran pembayaran = pembayaranService.getLast(pelangganEntity);
		assertEquals(Month.SEPTEMBER, pembayaran.getBulan());
		assertEquals(2014, pembayaran.getTahun());
	}
}
