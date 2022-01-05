package org.pahappa.systems.core.services.impl;

import javax.ws.rs.core.MediaType;

import org.pahappa.systems.models.SystemSetting;
import org.sers.webutils.model.Country;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.dao.CountryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.pahappa.systems.core.services.SystemSettingService;
import org.sers.webutils.model.exception.OperationFailedException;

@Service
@Transactional
public class SystemSettingServiceImpl extends GenericServiceImpl<SystemSetting> implements SystemSettingService {

    @Autowired
    CountryDao countryDao;

    @Override
    public SystemSetting saveInstance(SystemSetting appSetting) throws ValidationFailedException {
        return super.save(appSetting);
    }

    @Override
    public Country getCountryByName(String countryName) {
        return countryDao.searchUniqueByPropertyEqual("name", countryName);
    }

    @Override
    public SystemSetting getAppSetting() {
        if (super.findAll().size() > 0) {
            return super.findAll().get(0);
        } else {
            return super.save(new SystemSetting());
        }
    }

    public int testUrls(String url) {
        WebResource resource = Client.create(new DefaultClientConfig())
                .resource(url);
        final Builder webResource = resource.accept(MediaType.APPLICATION_JSON);
        webResource.type(MediaType.APPLICATION_JSON);
        ClientResponse clientResponse = webResource.post(ClientResponse.class, String.class.toString());
        System.out.println("Status " + clientResponse.getStatus());
        System.out.println("Response " + clientResponse.toString());
        return clientResponse.getStatus();
    }

    @Override
    public boolean isDeletable(SystemSetting entity) throws OperationFailedException {
        return true;
    }

}
