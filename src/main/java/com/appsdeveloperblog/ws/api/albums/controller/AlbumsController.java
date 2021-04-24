package com.appsdeveloperblog.ws.api.albums.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.client.RestTemplate;

import com.appsdeveloperblog.ws.api.albums.response.AlbumRest;

@Controller
public class AlbumsController {
	@Autowired
	OAuth2AuthorizedClientService service;

	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/albums")
	public String getAlbums(Model model, @AuthenticationPrincipal OidcUser principal) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

		if (token != null) {
			System.out.println("token ID" + token.getAuthorizedClientRegistrationId());
			System.out.println("token ID" + token.getName());
		} else {
			System.out.println("token is null " + token);
		}
		OAuth2AuthorizedClient oauth2client = service.loadAuthorizedClient(token.getAuthorizedClientRegistrationId(),
				token.getName());
		String jwtAccessToken = oauth2client.getAccessToken().getTokenValue();
		System.out.println("jwtAccessToken " + jwtAccessToken);
		OidcIdToken idToken = principal.getIdToken();
		String idTokenVal = idToken.getTokenValue();

		String url = "http://localhost:8082/albums";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + jwtAccessToken);
		HttpEntity<List<AlbumRest>> entity = new HttpEntity<>(headers);

		ResponseEntity<List<AlbumRest>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<AlbumRest>>() {
				});
		System.out.println("Response Entity " + entity);
		List<AlbumRest> list = responseEntity.getBody();

		/*
		 * List<AlbumRest> list = new ArrayList<AlbumRest>(); AlbumRest album = new
		 * AlbumRest("ID1", "AlbumID1", "Title", "Description", "Url"); AlbumRest album2
		 * = new AlbumRest("ID2", "AlbumID2", "Title", "Description", "Url"); AlbumRest
		 * album3 = new AlbumRest("ID3", "AlbumID3", "Title", "Description", "Url");
		 * list.add(album); list.add(album2); list.add(album3);
		 */

		model.addAttribute("albums", list);
		return "albums";
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
