package com.unitedvision.tvkabel.web.rest;

public class SimpanPembayaranRestRequest extends RestRequest {
	private int idPelanggan;
	private int idPegawai;
	private long jumlahPembayaran;
	private int jumlahBulan;
	
	public int getIdPelanggan() {
		return idPelanggan;
	}

	public void setIdPelanggan(int idPelanggan) {
		this.idPelanggan = idPelanggan;
	}

	public int getIdPegawai() {
		return idPegawai;
	}

	public void setIdPegawai(int idPegawai) {
		this.idPegawai = idPegawai;
	}

	public long getJumlahPembayaran() {
		return jumlahPembayaran;
	}

	public void setJumlahPembayaran(long jumlahPembayaran) {
		this.jumlahPembayaran = jumlahPembayaran;
	}

	public int getJumlahBulan() {
		return jumlahBulan;
	}

	public void setJumlahBulan(int jumlahBulan) {
		this.jumlahBulan = jumlahBulan;
	}
}
