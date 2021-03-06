package com.unitedvision.tvkabel.persistence.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unitedvision.tvkabel.exception.EmptyCodeException;
import com.unitedvision.tvkabel.exception.EmptyIdException;
import com.unitedvision.tvkabel.exception.StatusChangeException;
import com.unitedvision.tvkabel.persistence.entity.Pembayaran.Tagihan;
import com.unitedvision.tvkabel.util.DateUtil;

/**
 * Pelanggan domain.
 * 
 * @author Deddy Christoper Kakunsi
 *
 */
@Entity
@Table(name = "pelanggan")
public final class Pelanggan extends CodableDomain implements Removable {
	/** Registration number */
	private String nomorBuku;
	
	/** {@link Perusahaan} where customer subscribes */
	private Perusahaan perusahaan;

	/** Name */
	private String nama;
	
	/** Profession */
	private String profesi;
	
	/** Address */
	private Alamat alamat;
	
	/** Contact */
	private Kontak kontak;
	
	/** Detail */
	private Detail detail;
	
	/** Status */
	private Status status;

	/** {@link Kelurahan} where customer lives. */
	private Kelurahan kelurahan;

	/** List of {@link Pembayaran} made by customer. */
	private List<Pembayaran> listPembayaran;

	/**
	 * Pembayaran terakhir, set object ini setelah melakukan proses pembayaran.
	 */
	private Pembayaran pembayaranTerakhir;

	/**
	 * Create empty instance.
	 */
	public Pelanggan() {
		super();
	}

	/**
	 * Create instance.
	 * @param id must be positive
	 * @param perusahaan
	 * @param kode cannot be null or an empty string
	 * @param nama
	 * @param profesi
	 * @param kelurahan
	 * @param alamat
	 * @param kontak
	 * @param detail
	 * @param status
	 * @throws EmptyIdException {@code id} is negative.
	 * @throws EmptyCodeException {@code kode} is null or an empty string
	 */
	public Pelanggan(int id, String nomorBuku, Perusahaan perusahaan, String kode, String nama, String profesi, Kelurahan kelurahan, Alamat alamat, Kontak kontak, Detail detail, Status status) throws EmptyIdException, EmptyCodeException {
		super();
		setId(id);
		setNomorBuku(nomorBuku);
		setPerusahaan(perusahaan);
		setKode(kode);
		setNama(nama);
		setProfesi(profesi);
		setKelurahan(kelurahan);
		setAlamat(alamat);
		setKontak(kontak);
		setDetail(detail);
		setStatus(status);
	}

	@Override
	@Id @GeneratedValue
	public int getId() {
		return super.getId();
	}

	@Override
	@Column(name = "kode")
	public String getKode() {
		return super.getKode();
	}
	
	/**
	 * Return registration number.
	 * @return
	 */
	@Column(name = "nomor_buku")
	public String getNomorBuku() {
		return nomorBuku;
	}

	/**
	 * Set registration number.
	 * @param nomorBuku
	 */
	public void setNomorBuku(String nomorBuku) {
		this.nomorBuku = nomorBuku;
	}

	/**
	 * Return {@link Perusahaan} instance.
	 * @return perusahaan
	 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_perusahaan", referencedColumnName = "id")
	public Perusahaan getPerusahaan() {
		return perusahaan;
	}

	/**
	 * Set {@link Perusahaan} which customer subscribes.
	 * @param perusahaan
	 */
	public void setPerusahaan(Perusahaan perusahaan) {
		this.perusahaan = perusahaan;
	}

	/**
	 * Return name.
	 * @return nama
	 */
	@Column(name = "nama")
	public String getNama() {
		return nama;
	}

	/**
	 * Set name.
	 * @param nama
	 */
	public void setNama(String nama) {
		this.nama = nama;
	}

	/**
	 * Return profession.
	 * @return profesi
	 */
	@Column(name = "profesi")
	public String getProfesi() {
		return profesi;
	}

	/**
	 * Set profession.
	 * @param profesi
	 */
	public void setProfesi(String profesi) {
		this.profesi = profesi;
	}

	/**
	 * Return {@link Alamat} instance.
	 * @return alamat
	 */
	@JsonIgnore
	@Embedded
	public Alamat getAlamat() {
		return alamat;
	}

	/**
	 * Set {@link Alamat} where customer lives.
	 * @param alamat
	 */
	public void setAlamat(Alamat alamat) {
		this.alamat = alamat;
	}
	
	/**
	 * If alamat is null, create a new one.
	 */
	private void setAlamat() {
		if (alamat == null)
			alamat = new Alamat();
	}

	/**
	 * Return {@link Kontak} instance.
	 * @return kontak
	 */
	@JsonIgnore
	@Embedded
	public Kontak getKontak() {
		return kontak;
	}

	/**
	 * Set {@link Kontak} instance.
	 * @param kontak
	 */
	public void setKontak(Kontak kontak) {
		this.kontak = kontak;
	}
	
	/**
	 * If kontak is null, create a new one.
	 */
	private void setKontak() {
		if (kontak == null)
			kontak = new Kontak();
	}

	/**
	 * Return {@link Detail} instance.
	 * @return detail
	 */
	@JsonIgnore
	@Embedded
	public Detail getDetail() {
		return detail;
	}

	/**
	 * Set {@link Detail} instance.
	 * @param detail
	 */
	public void setDetail(Detail detail) {
		this.detail = detail;
	}
	
	/**
	 * If detail is null, create a new one.
	 */
	private void setDetail() {
		if (detail == null)
			detail = new Detail();
	}

	/**
	 * Return {@link Status} instance.
	 * @return status
	 */
	@Column(name = "status")
	public Status getStatus() {
		return status;
	}

	/**
	 * Set {@link Status} instance.
	 * @param status
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * Return {@link Kelurahan} where customer lives.
	 * @return
	 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_kelurahan", referencedColumnName = "id")
	public Kelurahan getKelurahan() {
		return kelurahan;
	}

	/**
	 * Set {@link Kelurahan} where customer lives.
	 * @param kelurahan
	 */
	public void setKelurahan(Kelurahan kelurahan) {
		this.kelurahan = kelurahan;
	}
	
	/**
	 * If kelurahan is null, create a new one.
	 */
	private void setKelurahan() {
		if (kelurahan == null)
			kelurahan = new Kelurahan();
	}

	/**
	 * Return list of {@link Pembayaran} made by customer.
	 * @return listPembayaran
	 */
	@JsonIgnore
	@OneToMany(targetEntity = Pembayaran.class, mappedBy = "pelanggan", fetch = FetchType.LAZY,
			cascade = {CascadeType.REMOVE})
	public List<Pembayaran> getListPembayaran() {
		return listPembayaran;
	}

	/**
	 * Set list of {@link Pembayaran} instances.
	 * @param listPembayaran
	 */
	public void setListPembayaran(List<Pembayaran> listPembayaran) {
		this.listPembayaran = listPembayaran;
	}

	/**
	 * Return {@code pembayaranTerakhir}.
	 * @return
	 */
	@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pembayaran_terakhir", referencedColumnName = "id")
	public Pembayaran getPembayaranTerakhir() {
		return pembayaranTerakhir;
	}

	public void setPembayaranTerakhir(Pembayaran pembayaranTerakhir) {
		this.pembayaranTerakhir = pembayaranTerakhir;
	}
	
	private void setPembayaranTerakhir() {
		if (pembayaranTerakhir == null)
			setPembayaranTerakhir(new Pembayaran());
	}
	
	@Transient
	public int getIdPembayaranTerakhir() {
		if (pembayaranTerakhir != null)
			return pembayaranTerakhir.getId();
		return 0;
	}
	
	public void setIdPembayaranTerakhir(int id) {
		if (id != 0) {
			setPembayaranTerakhir();
			try {
				pembayaranTerakhir.setId(id);
			} catch (EmptyIdException e) {
				e.printStackTrace();
			}
		}
	}

	@Transient
	public String getNamaKelurahan() {
		return kelurahan.getNama();
	}
	
	public void setNamaKelurahan(String namaKelurahan) {
		setKelurahan();
		kelurahan.setNama(namaKelurahan);
	}

	@Transient
	public int getIdKelurahan() {
		return kelurahan.getId();
	}
	
	public void setIdKelurahan(int idKelurahan) throws EmptyIdException {
		setKelurahan();
		kelurahan.setId(idKelurahan);
	}
	
	@Transient
	public String getNamaKecamatan() {
		return kelurahan.getNamaKecamatan();
	}
	
	public void setNamaKecamatan(String namaKecamatan) {
		setKelurahan();
		kelurahan.setNamaKecamatan(namaKecamatan);
	}

	@Transient
	public int getIdKecamatan() {
		return kelurahan.getIdKecamatan();
	}
	
	public void setIdKecamatan(int idKecamatan) throws EmptyIdException {
		setKelurahan();
		kelurahan.setIdKecamatan(idKecamatan);
	}
	
	@Transient
	public String getNamaKota() {
		return kelurahan.getNamaKota();
	}
	
	public void setNamaKota(String namaKota) {
		setKelurahan();
		kelurahan.setNamaKota(namaKota);
	}

	@Transient
	public int getIdKota() {
		return kelurahan.getIdKota();
	}
	
	public void setIdKota(int idKota) throws EmptyIdException {
		setKelurahan();
		kelurahan.setIdKota(idKota);
	}

	@Transient
	public int getLingkungan() {
		return alamat.getLingkungan();
	}
	
	public void setLingkungan(int lingkungan) {
		setAlamat();
		alamat.setLingkungan(lingkungan);
	}

	@Transient
	public String getDetailAlamat() {
		return alamat.getDetailAlamat();
	}
	
	public void setDetailAlamat(String detailAlamat) {
		setAlamat();
		alamat.setDetailAlamat(detailAlamat);
	}
	
	@Transient
	public float getLatitude() {
		return alamat.getLatitude();
	}
	
	public void setLatitude(float latitude) {
		setAlamat();
		alamat.setLatitude(latitude);
	}

	@Transient
	public float getLongitude() {
		return alamat.getLongitude();
	}
	
	public void setLongitude(float longitude) {
		setAlamat();
		alamat.setLongitude(longitude);
	}

	@Transient
	public String getTelepon() {
		return kontak.getTelepon();
	}
	
	public void setTelepon(String telepon) {
		setKontak();
		kontak.setTelepon(telepon);
	}

	@Transient
	public String getHp() {
		return kontak.getHp();
	}
	
	public void setHp(String hp) {
		setKontak();
		kontak.setHp(hp);
	}

	@Transient
	public String getEmail() {
		return kontak.getEmail();
	}
	
	public void setEmail(String email) {
		setKontak();
		kontak.setEmail(email);
	}
	
	@JsonIgnore
	@Transient
	public Date getTanggalMulai() {
		return detail.getTanggalMulai();
	}
	
	public void setTanggalMulai(Date tanggalMulai) {
		setDetail();
		detail.setTanggalMulai(tanggalMulai);
	}

	@Transient
	public String getTanggalMulaiStr() {
		return DateUtil.toString(getTanggalMulai());
	}
	
	public void setTanggalMulaiStr(String tanggalMulaiStr) {
		setTanggalMulai(DateUtil.getDate(tanggalMulaiStr));;
	}

	@Transient
	public int getJumlahTv() {
		return detail.getJumlahTv();
	}
	
	public void setJumlahTv(int jumlahTv) {
		setDetail();
		detail.setJumlahTv(jumlahTv);
	}

	@Transient
	public long getIuran() {
		return detail.getIuran();
	}
	
	public void setIuran(long iuran) {
		setDetail();
		detail.setIuran(iuran);
	}

	@Transient
	public int getTunggakan() {
		return detail.getTunggakan();
	}
	
	public void setTunggakan(int tunggakan) {
		setDetail();
		detail.setTunggakan(tunggakan);
	}

	public int countTunggakan() {
		if (pembayaranTerakhir == null)
			return countDefaultTunggakan();
		return countTunggakan(pembayaranTerakhir.getTagihan());
	}

	public int countDefaultTunggakan() {
		Tagihan def = Tagihan.create(detail.getTanggalMulai());

		return countTunggakan(def);
	}
	
	public int countTunggakan(Tagihan tagihan) {
		Tagihan now = Tagihan.create(DateUtil.getNow());
		detail.setTunggakan(((Comparable)now).compareWith(tagihan));
		
		return detail.getTunggakan();
	}

	@Override
	public void remove() throws StatusChangeException {
		if (isNew())
			throw new StatusChangeException("Pelanggan is new");
		
		if (status.equals(Status.REMOVED))
			throw new StatusChangeException("Pelanggan was already removed");
			
		setStatus(Status.REMOVED);
		try {
			setKode(String.format("REM%d", getId()));
		} catch (EmptyCodeException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@JsonIgnore
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((alamat == null) ? 0 : alamat.hashCode());
		result = prime * result + ((detail == null) ? 0 : detail.hashCode());
		result = prime * result
				+ ((kelurahan == null) ? 0 : kelurahan.hashCode());
		result = prime * result + ((kontak == null) ? 0 : kontak.hashCode());
		result = prime * result + ((nama == null) ? 0 : nama.hashCode());
		result = prime * result
				+ ((perusahaan == null) ? 0 : perusahaan.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	@JsonIgnore
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pelanggan other = (Pelanggan) obj;
		if (alamat == null) {
			if (other.alamat != null)
				return false;
		} else if (!alamat.equals(other.alamat))
			return false;
		if (detail == null) {
			if (other.detail != null)
				return false;
		} else if (!detail.equals(other.detail))
			return false;
		if (kelurahan == null) {
			if (other.kelurahan != null)
				return false;
		} else if (!kelurahan.equals(other.kelurahan))
			return false;
		if (kontak == null) {
			if (other.kontak != null)
				return false;
		} else if (!kontak.equals(other.kontak))
			return false;
		if (nama == null) {
			if (other.nama != null)
				return false;
		} else if (!nama.equals(other.nama))
			return false;
		if (perusahaan == null) {
			if (other.perusahaan != null)
				return false;
		} else if (!perusahaan.equals(other.perusahaan))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	@JsonIgnore
	public String toString() {
		return "Pelanggan [perusahaan=" + perusahaan + ", nama=" + nama
				+ ", alamat=" + alamat + ", kontak=" + kontak + ", detail="
				+ detail + ", status=" + status + ", kelurahan=" + kelurahan
				+ "]";
	}

	/**
	 * Customer's detail.
	 * 
	 * @author Deddy Christoper Kakunsi
	 *
	 */
	@Embeddable
	public static final class Detail {
		/** Subscription start date */
		private Date tanggalMulai;
		
		/** Number of television */
		private int jumlahTv;
		
		/** Monthly bill */
		private long iuran;
		
		/** Debt */
		private int tunggakan;
		
		/**
		 * Create empty instance.
		 */
		public Detail() {
			super();
		}

		/**
		 * Create instance.
		 * @param tanggalMulai
		 * @param jumlahTv
		 * @param iuranBulanan
		 * @param tunggakan
		 */
		public Detail(Date tanggalMulai, int jumlahTv, long iuranBulanan, int tunggakan) {
			super();
			setTanggalMulai(tanggalMulai);
			setJumlahTv(jumlahTv);
			setIuran(iuranBulanan);
			setTunggakan(tunggakan);
		}

		/**
		 * Return start date.
		 * @return tanggalMulai
		 */
		@Temporal(TemporalType.DATE)
		@Column(name = "tanggal_mulai")
		public Date getTanggalMulai() {
			return tanggalMulai;
		}

		/**
		 * Set start date.
		 * @param tanggalMulai
		 */
		public void setTanggalMulai(Date tanggalMulai) {
			this.tanggalMulai = tanggalMulai;
		}

		/**
		 * Return number of television.
		 * @return jumlahTv
		 */
		@Column(name = "jumlah_tv")
		public int getJumlahTv() {
			return jumlahTv;
		}

		/**
		 * Set number of television.
		 * @param jumlahTv
		 */
		public void setJumlahTv(int jumlahTv) {
			this.jumlahTv = jumlahTv;
		}

		/**
		 * Return monthly bill.
		 * @return iuran
		 */
		@Column(name = "iuran")
		public long getIuran() {
			return iuran;
		}

		/**
		 * Set monthly bill.
		 * @param iuran
		 */
		public void setIuran(long iuran) {
			this.iuran = iuran;
		}

		/**
		 * Set debt.
		 * @return tunggakan
		 */
		@Column(name = "tunggakan")
		public int getTunggakan() {
			return tunggakan;
		}

		/**
		 * Set debt.
		 * @param tunggakan
		 */
		public void setTunggakan(int tunggakan) {
			this.tunggakan = tunggakan;
		}

		@Override
		@JsonIgnore
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (iuran ^ (iuran >>> 32));
			result = prime * result + jumlahTv;
			result = prime * result
					+ ((tanggalMulai == null) ? 0 : tanggalMulai.hashCode());
			result = prime * result + tunggakan;
			return result;
		}

		@Override
		@JsonIgnore
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Detail other = (Detail) obj;
			if (iuran != other.iuran)
				return false;
			if (jumlahTv != other.jumlahTv)
				return false;
			if (tanggalMulai == null) {
				if (other.tanggalMulai != null)
					return false;
			} else if (!tanggalMulai.equals(other.tanggalMulai))
				return false;
			if (tunggakan != other.tunggakan)
				return false;
			return true;
		}

		@Override
		@JsonIgnore
		public String toString() {
			return "Detail [tanggalMulai=" + tanggalMulai + ", jumlahTv="
					+ jumlahTv + ", iuran=" + iuran + ", tunggakan="
					+ tunggakan + "]";
		}
	}
	/**
	 * Pelanggan status
	 * 
	 * @author Deddy Christoper Kakunsi
	 *
	 */
	public enum Status {
		/** AKTIF */
		AKTIF,
		/** PUTUS */
		BERHENTI,
		/** HUTANG */
		PUTUS,
		/** REMOVED from database (not deleted) */
		REMOVED,
		/** FREE OF CHARGE */
		GRATIS;
		
		/**
		 * Returns {@link Status} from the given string.
		 * @param status
		 * @return {@link Status}.
		 */
		public static Status get(String status) {
			status = status.toUpperCase();
			
			return Pelanggan.Status.valueOf(status);
		}
	}
}
