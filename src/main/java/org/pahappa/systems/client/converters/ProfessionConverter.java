package org.pahappa.systems.client.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.pahappa.systems.constants.Region;
import org.pahappa.systems.models.ProfessionValue;

import org.sers.webutils.model.Gender;

@FacesConverter("professionConverter")
public class ProfessionConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {

		
		return ProfessionValue.fromString(arg2);
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object object) {
		if (object == null || object instanceof String)
			return null;
		return ((ProfessionValue) object).getName();
	}
}
