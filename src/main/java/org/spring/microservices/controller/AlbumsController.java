package org.spring.microservices.controller;

import java.util.ArrayList;
import java.util.List;

import org.spring.microservices.response.AlbumRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AlbumsController {
	@Autowired
	OAuth2AuthorizedClientService service;

	@GetMapping("/albums")
	public String getAlbums(Model model, @AuthenticationPrincipal OidcUser principal) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
		OAuth2AuthorizedClient oauth2client = service.loadAuthorizedClient(token.getAuthorizedClientRegistrationId(),
				token.getName());
		String jwtAccessToken = oauth2client.getAccessToken().getTokenValue();
		System.out.println("Access token " + jwtAccessToken);
		OidcIdToken idToken = principal.getIdToken();
		String idTokenVal = idToken.getTokenValue();
		System.out.println("Id token " + idTokenVal);
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
