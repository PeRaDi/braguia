package pt.uminho.braguia.info;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppInfo {

    @SerializedName("app_name")
    private String appName;

    @SerializedName("socials")
    private List<Social> socials;

    @SerializedName("contacts")
    private List<Contact> contacts;

    @SerializedName("partners")
    private List<Partner> partners;

    @SerializedName("app_desc")
    private String appDesc;

    @SerializedName("app_landing_page_text")
    private String appLandingPageText;

    public class Social {
        @SerializedName("social_name")
        private String socialName;

        @SerializedName("social_url")
        private String socialUrl;

        @SerializedName("social_share_link")
        private String socialShareLink;

        @SerializedName("social_app")
        private String socialApp;
    }

    public class Contact {
        @SerializedName("contact_name")
        private String contactName;

        @SerializedName("contact_phone")
        private String contactPhone;

        @SerializedName("contact_url")
        private String contactUrl;

        @SerializedName("contact_mail")
        private String contactMail;

        @SerializedName("contact_desc")
        private String contactDesc;

        @SerializedName("contact_app")
        private String contactApp;
    }

    public class Partner {
        @SerializedName("partner_name")
        private String partnerName;

        @SerializedName("partner_phone")
        private String partnerPhone;

        @SerializedName("partner_url")
        private String partnerUrl;

        @SerializedName("partner_mail")
        private String partnerMail;

        @SerializedName("partner_desc")
        private String partnerDesc;

        @SerializedName("partner_app")
        private String partnerApp;
    }
}
