package com.example.hotel.controller.business;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.hotel.entity.business.BusinessProfile;
import com.example.hotel.service.business.BusinessProfileService;

@RestController
@CrossOrigin
public class BusinessProfileController {

	@Autowired
	private BusinessProfileService businessProfileService;
	
	@PostMapping("/businessProfile/save")
	public ResponseEntity<String> saveCandidate(@RequestParam("name") String name,
			@RequestParam("address") String address, @RequestParam("pincode") int pincode,
			@RequestParam("state") String state, @RequestParam("country") String country,
			@RequestParam("description") String description, @RequestParam("ownerName") String ownerName,
			@RequestParam("gstNumber") String gstNumber, @RequestParam("location") String location,
			@RequestParam("phoneNumber") long phoneNumber, @RequestParam("email") String email,
			@RequestParam(value = "profile", required = false) MultipartFile profile,
			@RequestParam(value = "logo", required = false) MultipartFile logo) throws SQLException {

		try {
			BusinessProfile businessProfile = new BusinessProfile();
			businessProfile.setAddress(address);
			businessProfile.setName(name);
			businessProfile.setCountry(country);
			businessProfile.setPincode(pincode);
			businessProfile.setState(state);
			businessProfile.setLocation(location);
			businessProfile.setPhoneNumber(phoneNumber);
			businessProfile.setEmail(email);
			businessProfile.setDescription(description);
			businessProfile.setOwnerName(ownerName);
			businessProfile.setGstNumber(gstNumber);
			businessProfile.setProfile(convertToBlob(profile));
			businessProfile.setLogo(convertToBlob(logo));
			businessProfileService.SaveProfileDetails(businessProfile);

			return ResponseEntity.ok("BusinessProfile Details saved successfully.");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving the businessProfile: " + e.getMessage());
		}
	}

	private Blob convertToBlob(MultipartFile file) throws IOException, SQLException {
		if (file != null && !file.isEmpty()) {
			byte[] bytes = file.getBytes();
			return new javax.sql.rowset.serial.SerialBlob(bytes);
		} else {
			return null;
		}
	}

	@GetMapping("/profile/view")
	public ResponseEntity<?> displayAllImages(@RequestParam(required = true) String businessProfile) {
		if ("profileDetails".equals(businessProfile)) {
			List<BusinessProfile> profiles = businessProfileService.listAll();
			List<Map<String, Object>> profileObjects = new ArrayList<>();
			for (BusinessProfile profile1 : profiles) {
				Map<String, Object> profileObject = new HashMap<>();
				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(profile1);
				String fileExtension1 = getFileExtensionForImage1(profile1);
				String url = "profile/" + randomNumber + "/" + profile1.getProfileId() + "." + fileExtension;
				String logoUrl = "logo/" + randomNumber + "/" + profile1.getProfileId() + "." + fileExtension1;
				profile1.setProfileUrl(url);
				profile1.setLogoUrl(logoUrl);
				profileObject.put("profileId", profile1.getProfileId());
				profileObject.put("name", profile1.getName());
				profileObject.put("logoUrl", profile1.getLogoUrl());
				profileObject.put("profileUrl", profile1.getProfileUrl());
				profileObject.put("address", profile1.getAddress());
				profileObject.put("country", profile1.getCountry());
				profileObject.put("email", profile1.getEmail());
				profileObject.put("state", profile1.getState());
				profileObject.put("pincode", profile1.getPincode());
				profileObject.put("location", profile1.getLocation());
				profileObject.put("phoneNumber1", profile1.getPhoneNumber());
				profileObject.put("ownerName", profile1.getOwnerName());
				profileObject.put("gstNumber", profile1.getGstNumber());
				profileObject.put("description", profile1.getDescription());
				profileObjects.add(profileObject);
			}
			return ResponseEntity.ok(profileObjects);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access");
		}

	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
	}

	@GetMapping("logo/{randomNumber}/{id:.+}")
	public ResponseEntity<Resource> serveImage(@PathVariable("randomNumber") int randomNumber,
			@PathVariable("id") String id) {
		String[] parts = id.split("\\.");
		if (parts.length != 2) {
			return ResponseEntity.badRequest().build();
		}
		String fileExtension = parts[1];
		Long imageId;
		try {
			imageId = Long.parseLong(parts[0]);
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().build(); // Invalid image ID format
		}

		BusinessProfile image = businessProfileService.findById(imageId);
		if (image == null || image.getLogo() == null) {
			return ResponseEntity.notFound().build();
		}

		byte[] imageBytes;
		try {
			imageBytes = image.getLogo().getBytes(1, (int) image.getLogo().length());
		} catch (SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		ByteArrayResource resource = new ByteArrayResource(imageBytes);
		HttpHeaders headers = new HttpHeaders();

		if ("jpg".equalsIgnoreCase(fileExtension)) {
			headers.setContentType(MediaType.IMAGE_JPEG);
		} else if ("png".equalsIgnoreCase(fileExtension)) {
			headers.setContentType(MediaType.IMAGE_PNG);
		} else {
			headers.setContentType(MediaType.IMAGE_JPEG);
		}
		return ResponseEntity.ok().headers(headers).body(resource);
	}

	@GetMapping("profile/{randomNumber}/{id:.+}")
	public ResponseEntity<Resource> serveImages(@PathVariable("randomNumber") int randomNumber,
			@PathVariable("id") String id) {
		String[] parts = id.split("\\.");
		if (parts.length != 2) {
			return ResponseEntity.badRequest().build();
		}
		String fileExtension = parts[1];
		Long imageId;
		try {
			imageId = Long.parseLong(parts[0]);
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().build(); // Invalid image ID format
		}
		BusinessProfile image = businessProfileService.findById(imageId);
		if (image == null) {
			return ResponseEntity.notFound().build();
		}
		byte[] imageBytes;

		try {
			imageBytes = image.getProfile().getBytes(1, (int) image.getProfile().length());
		} catch (SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		ByteArrayResource resource = new ByteArrayResource(imageBytes);
		HttpHeaders headers = new HttpHeaders();

		if ("jpg".equalsIgnoreCase(fileExtension)) {
			headers.setContentType(MediaType.IMAGE_JPEG);
		} else if ("png".equalsIgnoreCase(fileExtension)) {
			headers.setContentType(MediaType.IMAGE_PNG);
		} else {
			headers.setContentType(MediaType.IMAGE_JPEG);
		}
		return ResponseEntity.ok().headers(headers).body(resource);
	}

	private String getFileExtensionForImage(BusinessProfile image) {
		if (image == null || image.getProfileUrl() == null || image.getProfileUrl().isEmpty()) {
			return "jpg";
		}

		String url = image.getProfileUrl();
		if (url.endsWith(".png")) {
			return "png";
		} else if (url.endsWith(".jpg")) {
			return "jpg";
		} else {
			return "jpg";
		}

	}

	private String getFileExtensionForImage1(BusinessProfile image) {
		if (image == null || image.getLogoUrl() == null || image.getLogoUrl().isEmpty()) {
			return "jpg";
		}

		String url = image.getLogoUrl();
		if (url.endsWith(".png")) {
			return "png";
		} else if (url.endsWith(".jpg")) {
			return "jpg";
		} else {
			return "jpg";
		}
	}

	@PutMapping("/profile/edit/{profileId}")
	public ResponseEntity<String> updateProfile(@PathVariable long profileId,
			@RequestParam(value = "profile", required = false) MultipartFile profile,
			@RequestParam(value = "logo", required = false) MultipartFile logo,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "country", required = false) String country,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "gstNumber", required = false) String gstNumber,
			@RequestParam(value = "ownerName", required = false) String ownerName,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "location", required = false) String location,
			@RequestParam(value = "pincode", required = false) Integer pincode,
			@RequestParam(value = "phoneNumber", required = false) Long phoneNumber) {
		try {

			BusinessProfile businessProfile = businessProfileService.findById(profileId);
			if (businessProfile == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found.");
			}

			if (profile != null && !profile.isEmpty()) {
				byte[] bytes = profile.getBytes();
				Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
				businessProfile.setProfile(blob);
			}

			if (logo != null && !logo.isEmpty()) {
				byte[] bytes = logo.getBytes();
				Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
				businessProfile.setLogo(blob);
			}

			if (name != null) {
				businessProfile.setName(name);
			}

			if (country != null) {
				businessProfile.setCountry(country);
			}

			if (email != null) {
				businessProfile.setEmail(email);
			}

			if (location != null) {
				businessProfile.setLocation(location);
			}

			if (state != null) {
				businessProfile.setState(state);
			}

			if (address != null) {
				businessProfile.setAddress(address);
			}

			if (phoneNumber != null) {
				businessProfile.setPhoneNumber(phoneNumber);
			}

			if (pincode != null) {
				businessProfile.setPincode(pincode);
			}
			if (ownerName != null) {
				businessProfile.setOwnerName(ownerName);
			}
			if (gstNumber != null) {
				businessProfile.setGstNumber(gstNumber);
			}
			if (description != null) {
				businessProfile.setDescription(description);
			}
			businessProfileService.SaveProfileDetails(businessProfile);
			return ResponseEntity.ok("Profile updated successfully. Profile ID: " + profileId);
		} catch (IOException | SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating profile.");
		}
	}

	@DeleteMapping("/profile/delete/{profileId}")
	public ResponseEntity<String> deleteProfile(@PathVariable("profileId") Long profileId) {
		businessProfileService.deleteProfileId(profileId);
		return ResponseEntity.ok("Profile details deleted successfully");
	}
	
}
