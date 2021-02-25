package com.fajar.livestreaming.service.entity;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.fajar.livestreaming.dto.WebResponse;
import com.fajar.livestreaming.entity.ApplicationProfile;

@Service
public class ShopProfileUpdateService extends BaseEntityUpdateService<ApplicationProfile>{
 
	
	@Override
	public ApplicationProfile saveEntity(ApplicationProfile baseEntity, boolean newRecord, HttpServletRequest httpServletRequest){
		ApplicationProfile shopProfile = (ApplicationProfile) copyNewElement(baseEntity, newRecord);
		 
		validateEntityFormFields(shopProfile, newRecord, httpServletRequest);
//		if (base64Image != null && !base64Image.equals("")) {
//			try {
//				String imageName = fileService.writeImage(baseEntity.getClass().getSimpleName(), base64Image);
//				shopProfile.setIconUrl(imageName);
//			} catch (IOException e) {
//
//				shopProfile.setIconUrl(null);
//				e.printStackTrace();
//			}
//		} else {
//			if (!newRecord) {
//				Optional<Profile> dbShopProfile = shopProfileRepository.findById(shopProfile.getId());
//				if (dbShopProfile.isPresent()) {
//					shopProfile.setIconUrl(dbShopProfile.get().getIconUrl());
//				}
//			}
//		}
		ApplicationProfile newShopProfile = entityRepository.save(shopProfile);
		return newShopProfile;
	}
	
}

