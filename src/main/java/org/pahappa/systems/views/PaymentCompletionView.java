package org.pahappa.systems.views;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.pahappa.systems.security.HyperLinks;
import org.sers.webutils.client.controllers.WebAppExceptionHandler;
import org.sers.webutils.client.views.presenters.ViewPath;

@ManagedBean(name = "paymentCompletionView")
@ViewScoped
@ViewPath(path = HyperLinks.PAYMENT_COMPLETION_VIEW)
public class PaymentCompletionView extends WebAppExceptionHandler implements Serializable {

    private static final long serialVersionUID = 1L;
    private Boolean successfullPaymet;
    private String logoutUrl = "ServiceLogout";

    @SuppressWarnings("unused")
    private String viewPath;

    private String title, description;

    @PostConstruct
    public void init() {
        this.successfullPaymet = true;
        if (successfullPaymet) {
            this.title = "Payment complete";
            this.description = "Your transaction is complete, Check your email for login credentials";
        } else {
            this.title = "Payment failed";
            this.description = "Registartion falied please try again";

        }
    }

    public Boolean getSuccessfullPaymet() {
        return successfullPaymet;
    }

    public void setSuccessfullPaymet(Boolean successfullPaymet) {
        this.successfullPaymet = successfullPaymet;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String getViewPath() {
        return viewPath;
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
