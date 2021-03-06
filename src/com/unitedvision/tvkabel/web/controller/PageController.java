package com.unitedvision.tvkabel.web.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unitedvision.tvkabel.core.service.KelurahanService;
import com.unitedvision.tvkabel.exception.ApplicationException;
import com.unitedvision.tvkabel.exception.EntityNotExistException;
import com.unitedvision.tvkabel.persistence.entity.Perusahaan;
import com.unitedvision.tvkabel.util.CodeUtil;
import com.unitedvision.tvkabel.util.DateUtil;
import com.unitedvision.tvkabel.web.rest.RestResult;

@Controller
public class PageController extends AbstractController {
	@Autowired
	private KelurahanService kelurahanService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showHome() {
		return "redirect:/admin";
	}	
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String showHomeAdmin(@RequestParam(required = false) String message, Map<String, Object> model) {
		return "cover_contact";
	}
	
	@RequestMapping(value = "/admin/tunggakan/recount/{kode}", method = RequestMethod.GET)
	public @ResponseBody RestResult recountTunggakan(@PathVariable String kode, Map<String, Object> model) {
		String message;
		try {
			if (!kode.equals(CodeUtil.getKode())) {
				message = "Gagal! Anda tidak punya otoritas!";
			} else {
				pelangganService.recountTunggakan();
				message = "Berhasil!";
			}
		} catch (ApplicationException e) {
			message = String.format("Gagal! %s", e.getMessage());
		}

		return RestResult.create(message);
	}
	
	@RequestMapping(value = "/admin/tunggakan/recount/{kode}/{tanggal}", method = RequestMethod.GET)
	public @ResponseBody RestResult recountTunggakanWithTanggal(@PathVariable String kode, @PathVariable String tanggal, Map<String, Object> model) {
		String message;
		try {
			if (!kode.equals(CodeUtil.getKode())) {
				message = "Gagal! Anda tidak memiliki otoritas!";
			} else {
				pelangganService.recountTunggakan(tanggal);
				message = "Berhasil!";
			}
		} catch (ApplicationException e) {
			message = String.format("Gagal! %s", e.getMessage());
		}

		return RestResult.create(message);
	}

	@RequestMapping(value = "/admin/tunggakan/recount/now/{kode}", method = RequestMethod.GET)
	public @ResponseBody RestResult recountTunggakanNow(@PathVariable String kode, Map<String, Object> model) {
		String message;
		try {
			if (!kode.equals(CodeUtil.getKode())) {
				message = "Gagal! Anda tidak punya otoritas!";
			} else {
				Date now = DateUtil.getNow();
				String tanggal = DateUtil.getDayString(now);

				pelangganService.recountTunggakan(tanggal);
				message = "Berhasil!";
			}
		} catch (ApplicationException e) {
			message = String.format("Gagal! %s", e.getMessage());
		}

		return RestResult.create(message);
	}
	
	@RequestMapping(value = "/admin/kode/reset/{kode}/{idPerusahaan}", method = RequestMethod.GET)
	public @ResponseBody RestResult resetAllKode(@PathVariable String kode, @PathVariable Integer idPerusahaan) {
		String message;

		try {
			Perusahaan perusahaan = perusahaanService.getOne(idPerusahaan);
			
			if (!kode.equals(CodeUtil.getKode())) {
				message = "Gagal! Anda tidak memiliki otoritas!";
			} else {
				message = pelangganService.resetKode(perusahaan);
			}
			
			return RestResult.create(message);
		} catch (EntityNotExistException e) {
			return RestResult.create(e.getMessage());
		}
	}
}
