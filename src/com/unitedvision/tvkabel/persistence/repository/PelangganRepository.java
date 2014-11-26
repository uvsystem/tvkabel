package com.unitedvision.tvkabel.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unitedvision.tvkabel.core.domain.Pelanggan.Status;
import com.unitedvision.tvkabel.exception.EntityNotExistException;
import com.unitedvision.tvkabel.persistence.entity.KelurahanEntity;
import com.unitedvision.tvkabel.persistence.entity.PelangganEntity;
import com.unitedvision.tvkabel.persistence.entity.PerusahaanEntity;

public interface PelangganRepository extends JpaRepository<PelangganEntity, Integer> {
	String findByPembayaran = "SELECT * FROM pelanggan WHERE id in (SELECT DISTINCT id_pelanggan FROM pembayaran WHERE tanggal_bayar = :tanggalBayar AND id_pegawai = :idPegawai)";
	String findByTanggalMulai = "SELECT * FROM pelanggan p WHERE p.status = :status AND p.tanggal_mulai like %:tanggal";
	String countByPembayaran = "SELECT count(*) FROM pelanggan WHERE id in (SELECT DISTINCT id_pelanggan FROM pembayaran WHERE tanggal_bayar = :tanggalBayar AND id_pegawai = :idPegawai)";
	String summerizeEstimasiPemasukanBulanan = "SELECT SUM(p.detail.iuran) FROM PelangganEntity p WHERE p.perusahaan = ?1 AND p.status = ?2";
	String summerizeTotalEstimasiTunggakan = "SELECT SUM(p.detail.tunggakan * p.detail.iuran) FROM PelangganEntity p WHERE p.perusahaan = ?1";
	
	PelangganEntity findByPerusahaanAndKode(PerusahaanEntity perusahaanEntity, String kode) throws EntityNotExistException;
	PelangganEntity findByPerusahaanAndNama(PerusahaanEntity perusahaanEntity, String nama) throws EntityNotExistException;

	List<PelangganEntity> findByPerusahaanAndKodeContaining(PerusahaanEntity perusahaanEntity, String kode, Pageable page);
	List<PelangganEntity> findByPerusahaanAndNamaContaining(PerusahaanEntity perusahaanEntity, String nama, Pageable page);
	List<PelangganEntity> findByPerusahaanAndStatus(PerusahaanEntity perusahaanEntity, Status status);
	List<PelangganEntity> findByPerusahaanAndStatus(PerusahaanEntity perusahaanEntity, Status status, Pageable page);
	List<PelangganEntity> findByPerusahaanAndStatusAndNamaContaining(PerusahaanEntity perusahaanEntity, Status status, String nama, Pageable page);
	List<PelangganEntity> findByPerusahaanAndStatusAndKodeContaining(PerusahaanEntity perusahaanEntity, Status status, String kode, Pageable page);

	List<PelangganEntity> findByPerusahaanAndStatusAndKelurahanAndAlamat_Lingkungan(PerusahaanEntity perusahaanEntity, Status status, KelurahanEntity kelurahanEntity, int lingkungan);
	List<PelangganEntity> findByPerusahaanAndStatusAndKelurahanAndAlamat_Lingkungan(PerusahaanEntity perusahaanEntity, Status status, KelurahanEntity kelurahanEntity, int lingkungan, Pageable page);

	List<PelangganEntity> findByPerusahaanAndStatusAndDetail_Tunggakan(PerusahaanEntity perusahaanEntity, Status status, int tunggakan);
	List<PelangganEntity> findByPerusahaanAndStatusAndDetail_Tunggakan(PerusahaanEntity perusahaanEntity, Status status, int tunggakan, Pageable page);

	long countByPerusahaanAndStatusAndNamaContaining(PerusahaanEntity perusahaanEntity, Status status, String nama);
	long countByPerusahaanAndStatusAndKodeContaining(PerusahaanEntity perusahaanEntity, Status status, String kode);
	long countByPerusahaanAndStatus(PerusahaanEntity perusahaanEntity, Status status);
	long countByPerusahaanAndKodeContaining(PerusahaanEntity perusahaanEntity, String kode);
	long countByPerusahaanAndNamaContaining(PerusahaanEntity perusahaanEntity, String nama);
	
	long countByPerusahaanAndStatusAndKelurahanAndAlamat_Lingkungan(PerusahaanEntity perusahaanEntity, Status status, KelurahanEntity kelurahanEntity, int lingkungan);
	long countByPerusahaanAndStatusAndDetail_Tunggakan(PerusahaanEntity perusahaanEntity, Status status, int tunggakan);
	long countByPerusahaanAndStatusAndDetail_TunggakanGreaterThan(PerusahaanEntity perusahaanEntity, Status status, int tunggakan);
	long countByPerusahaanAndStatusAndDetail_TunggakanLessThan(PerusahaanEntity perusahaanEntity, Status status, int tunggakan);
	
	@Query (value = findByTanggalMulai, nativeQuery = true)
	List<PelangganEntity> findByTanggalMulai(@Param("status") Status status, @Param("tanggal") int tanggal);
	@Query (value = findByPembayaran, nativeQuery = true)
	List<PelangganEntity> findByPembayaran(@Param("idPegawai") int idPegawai, @Param("tanggalBayar") String tanggalBayar);

	@Query (value = countByPembayaran, nativeQuery = true)
	long countByPembayaran(@Param("idPegawai") int idPegawai, @Param("tanggalBayar") String tanggalBayar);

	@Query (summerizeEstimasiPemasukanBulanan)
	long sumarizeEstimasiPemasukanBulanan(PerusahaanEntity perusahaanEntity, Status status);
	@Query (summerizeTotalEstimasiTunggakan)
	long summarizeTotalAkumulasiTunggakan(PerusahaanEntity perusahaanEntity);
}
