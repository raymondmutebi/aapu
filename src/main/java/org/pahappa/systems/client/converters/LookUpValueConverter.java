package org.pahappa.systems.client.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.pahappa.systems.core.services.LookUpService;
import org.pahappa.systems.models.LookUpValue;
import org.sers.webutils.model.Country;
import org.sers.webutils.model.Gender;
import org.sers.webutils.server.core.service.SetupService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

/**
 * Converter class for {@link LookUpValue}. It inherits from {@link GenericConverter}
 * which implements all the required methods.
 * 
 * @author RayGdhrt
 *
 */
@FacesConverter("lookUpValueConverter")
public class LookUpValueConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		if (arg2 == null || arg2.isEmpty())
			return null;
                
		return ApplicationContextProvider.getBean(LookUpService.class).getLookUpValueById(arg2);
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object object) {
		if (object == null || object instanceof String)
			return null;
		return ((LookUpValue) object).getId();
	}
}
