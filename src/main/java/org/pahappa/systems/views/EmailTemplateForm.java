package org.pahappa.systems.views;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.pahappa.systems.core.services.EmailTemplateService;
import org.pahappa.systems.models.EmailTemplate;
import org.pahappa.systems.constants.TemplateType;
import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

@ManagedBean(name = "emailTemplateForm")
@SessionScoped
@ViewPath(path = HyperLinks.MAIL_TEMPLATE_FORM)
public class EmailTemplateForm extends WebFormView<EmailTemplate, EmailTemplateForm, EmailTemplateView> {

	private static final long serialVersionUID = 1L;
	private EmailTemplateService emailTemplateService;
	private List<TemplateType> templateTypes;

	@Override
	public void beanInit() {
		resetModal();
		this.emailTemplateService = ApplicationContextProvider.getBean(EmailTemplateService.class);
		this.templateTypes = Arrays.asList(TemplateType.values());
	}

	@Override
	public void pageLoadInit() {

	}

	@Override
	public void persist() throws ValidationFailedException, OperationFailedException {
		this.emailTemplateService.saveInstance(super.model);
	}

	public void resetModal() {
		super.resetModal();
		super.model = new EmailTemplate();
	}

	public List<TemplateType> getTemplateTypes() {
		return templateTypes;
	}

	public void setTemplateTypes(List<TemplateType> templateTypes) {
		this.templateTypes = templateTypes;
	}
}
