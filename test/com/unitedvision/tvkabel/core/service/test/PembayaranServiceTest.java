package com.unitedvision.tvkabel.core.service.test;

import static org.junit.Assert.*;

import java.time.Month;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.unitedvision.tvkabel.core.service.PegawaiService;
import com.unitedvision.tvkabel.core.service.PelangganService;
import com.unitedvision.tvkabel.core.service.PembayaranService;
import com.unitedvision.tvkabel.core.service.PerusahaanService;
import com.unitedvision.tvkabel.exception.ApplicationException;
import com.unitedvision.tvkabel.exception.DataDuplicationException;
import com.unitedvision.tvkabel.exception.EmptyIdException;
import com.unitedvision.tvkabel.exception.EntityNotExistException;
import com.unitedvision.tvkabel.exception.NotPayableCustomerException;
import com.unitedvision.tvkabel.exception.UnpaidBillException;
import com.unitedvision.tvkabel.persistence.SpringDataJpaConfig;
import com.unitedvision.tvkabel.persistence.entity.Pegawai;
import com.unitedvision.tvkabel.persistence.entity.Pelanggan;
import com.unitedvision.tvkabel.persistence.entity.Pembayaran;
import com.unitedvision.tvkabel.persistence.entity.Pembayaran.Tagihan;

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
	public void testDelete() throws ApplicationException {
		Pelanggan pelanggan = pelangganService.getOne(35);
		Pembayaran pembayaranTerakhir = pelanggan.getPembayaranTerakhir();
		assertEquals(2015, pembayaranTerakhir.getTahun());
		assertEquals(Month.FEBRUARY, pembayaranTerakhir.getBulan());
		assertEquals(0, pelanggan.getTunggakan());

		pembayaranService.delete(pembayaranTerakhir);

		Pelanggan pelangganDeleted = pelangganService.getOne(35);
		Pembayaran pembayaranTerakhirUpdated = pelangganDeleted.getPembayaranTerakhir();
		assertEquals(2015, pembayaranTerakhirUpdated.getTahun());
		assertEquals(Month.JANUARY, pembayaranTerakhirUpdated.getBulan());
		assertNotNull(pelangganDeleted);
		assertEquals(1, pelangganDeleted.getTunggakan());
	}
	
	@Test
	public void testGetPayableTagihan() throws EntityNotExistException {
		Pelanggan pelanggan = pelangganService.getOne(35);
		Tagihan tagihan = pembayaranService.getPayableTagihan(pelanggan);
		
		assertEquals(Month.MARCH, tagihan.getBulan());
		assertEquals(2015, tagihan.getTahun());
	}
	
	@Test
	public void testGetLast() throws EntityNotExistException {
		Pelanggan pelanggan = pelangganService.getOne(35);

		Pembayaran pembayaran = pembayaranService.getLast(pelanggan);
		assertEquals(Month.FEBRUARY, pembayaran.getBulan());
		assertEquals(2015, pembayaran.getTahun());
	}
	
	@Test
	public void testSinglePay() throws EntityNotExistException, NotPayableCustomerException, UnpaidBillException, DataDuplicationException, EmptyIdException {
		Pelanggan pelanggan = pelangganService.getOne(35);
		Pegawai pegawai = pegawaiService.getOne(15);
		long jumlahPembayaran = pelanggan.getIuran();
		int jumlahBulan = 1;

		assertEquals(0, pelanggan.getTunggakan());
		Pembayaran last = pembayaranService.pay(pelanggan, pegawai, jumlahPembayaran, jumlahBulan);
		Pelanggan pelangganUpdated = pelangganService.getOne(35);
		
		assertEquals(-1, pelangganUpdated.getTunggakan());
		assertEquals(last, pelangganUpdated.getPembayaranTerakhir());
	}
	
	@Test
	public void testMultiplePay() throws EntityNotExistException, NotPayableCustomerException, UnpaidBillException, DataDuplicationException, EmptyIdException {
		Pelanggan pelanggan = pelangganService.getOne(35);
		Pegawai pegawai = pegawaiService.getOne(15);
		long jumlahPembayaran = pelanggan.getIuran();
		int jumlahBulan = 3;

		assertEquals(0, pelanggan.getTunggakan());
		Pembayaran last = pembayaranService.pay(pelanggan, pegawai, jumlahPembayaran, jumlahBulan);
		Pelanggan pelangganUpdated = pelangganService.getOne(35);
		
		assertEquals(-jumlahBulan, pelangganUpdated.getTunggakan());
		assertEquals(last, pelangganUpdated.getPembayaranTerakhir());
	}
	
	@Test
	public void testCreateListPembayaran() throws EntityNotExistException, NotPayableCustomerException, UnpaidBillException, EmptyIdException, DataDuplicationException {
		Pelanggan pelanggan = pelangganService.getOne(35);
		Pegawai pegawai = pegawaiService.getOne(15);
		long jumlahPembayaran = pelanggan.getIuran();
		int jumlahBulan = 3;

		List<Pembayaran> listPembayaran = pembayaranService.createListPembayaran(pelanggan, pegawai, jumlahPembayaran, jumlahBulan);
		assertEquals(3, listPembayaran.size());

		Tagihan before = null;
		for (Pembayaran pembayaran : listPembayaran) {
			Tagihan tagihan = pembayaran.getTagihan();
			
			if (before != null) {
				assertNotEquals(before, tagihan);
			}
			
			before = tagihan;
		}
	}
}
