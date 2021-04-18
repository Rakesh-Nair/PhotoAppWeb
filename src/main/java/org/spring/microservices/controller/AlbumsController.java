package org.spring.microservices.controller;

import java.util.ArrayList;
import java.util.List;

import org.spring.microservices.response.AlbumRest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AlbumsController {

	@GetMapping("/albums")
	public String getAlbums(Model model) {
		List<AlbumRest> list = new ArrayList<AlbumRest>();
		AlbumRest album = new AlbumRest("ID1", "AlbumID1", "Title", "Description", "Url");
		AlbumRest album2 = new AlbumRest("ID2", "AlbumID2", "Title", "Description", "Url");
		AlbumRest album3 = new AlbumRest("ID3", "AlbumID3", "Title", "Description", "Url");
		list.add(album);
		list.add(album2);
		list.add(album3);
		model.addAttribute("albums", list);
		return "albums";
	}
}
