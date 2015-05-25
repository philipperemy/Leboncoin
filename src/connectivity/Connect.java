package connectivity;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import log.Logger;
import run.main.App;
import utils.NameFactory;
import utils.StringUtils;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlFileInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import conf.DefaultUserConf;

public class Connect
{
    public boolean upload(String _regionId, String _departementId, String _codePostal, String _category, String _nom, String _email, String _phone, String _subject, String _body, String _price, String _imgPath)
        throws FailingHttpStatusCodeException, MalformedURLException, IOException
    {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("Sending Request [ region id: " + _regionId);
        sBuilder.append(", department id: " + _departementId);
        sBuilder.append(", zipcode : " + _codePostal);
        sBuilder.append(", category id: " + _category);
        sBuilder.append(", name : " + _nom);
        sBuilder.append(", email : " + _email);
        sBuilder.append(", phone : " + _phone);
        sBuilder.append(", subject : " + _subject);
        sBuilder.append(", body : " + _body);
        sBuilder.append(", price : " + _price);
        sBuilder.append(", img path : " + _imgPath);
        sBuilder.append(" ]");

        Logger.traceINFO(sBuilder.toString());

        WebClient client = Client.get();

        final HtmlPage formPage = client.getPage(LinkFactory.FORM_LINK);

        {
            final HtmlSelect region = formPage.getElementByName(NameFactory.REGION_TAG_NAME);
            HtmlOption regionSelect = region.getOptionByValue(_regionId);
            region.setSelectedAttribute(regionSelect, true);
        }

        {
            final HtmlSelect departement = formPage.getElementByName(NameFactory.DEPARTMENT_TAG_NAME);
            HtmlOption dptSelect = departement.getOptionByValue(_departementId);
            departement.setSelectedAttribute(dptSelect, true);
        }

        {
            final HtmlTextInput codePostal = formPage.getElementByName(NameFactory.CODE_POSTAL_TAG_NAME);
            codePostal.setValueAttribute(_codePostal);
        }

        {
            final HtmlSelect category = formPage.getElementByName(NameFactory.CATEGORY_TAG_NAME);
            HtmlOption categorySelect = category.getOptionByValue(_category);
            category.setSelectedAttribute(categorySelect, true);
        }

        {
            final HtmlTextInput nom = formPage.getElementByName(NameFactory.NAME_TAG_NAME);
            nom.setValueAttribute(_nom);
        }

        {
            final HtmlTextInput email = formPage.getElementByName(NameFactory.EMAIL_TAG_NAME);
            email.setValueAttribute(_email);
        }

        {
            final HtmlTextInput phone = formPage.getElementByName(NameFactory.PHONE_TAG_NAME);
            phone.setValueAttribute(_phone);
        }

        {
            final HtmlTextInput subject = formPage.getElementByName(NameFactory.SUBJECT_TAG_NAME);
            subject.setValueAttribute(_subject);
        }

        {
            final HtmlTextArea body = formPage.getElementByName(NameFactory.BODY_TAG_NAME);
            body.setText(_body);
        }

        {
            final HtmlTextInput price = formPage.getElementByName(NameFactory.PRICE_TAG_NAME);
            price.setValueAttribute(_price);
        }

        try
        {
            HtmlFileInput fileInput = formPage.getElementByName(NameFactory.IMAGE_0);
            File jpg = new File("img/" + _imgPath);
            if (!jpg.exists())
            {
                Logger.traceERROR("File : " + _imgPath + " does not exist in img folder. Program will exit.");
                App.kill();
            }
            Logger.traceINFO(jpg.getAbsolutePath());
            fileInput.setValueAttribute(jpg.getAbsolutePath());
            try
            {
                Thread.sleep(10000);
            }
            catch (InterruptedException e)
            {
                Logger.traceERROR(e);
            }
        }
        catch (ElementNotFoundException enfe)
        {
            // Do nothing. It's a normal behavior.
            String pageStr = formPage.asXml();
            final String thumbUrlPattern = "<div class=\"photo\" style=\"background-image: url('";
            if (pageStr.contains(thumbUrlPattern))
            {
                pageStr = StringUtils.truncBeforeAndOverSymbol(pageStr, thumbUrlPattern);
                pageStr = StringUtils.truncAfter(pageStr, "');");
                Logger.traceINFO("Image successfully uploaded at : " + pageStr);
            }
        }

        {
            final HtmlSubmitInput submit = formPage.getElementByName(NameFactory.SUBMIT_BUTTON);
            HtmlPage submitPage = submit.click();
            if (submitPage.asText().toLowerCase().contains("le contenu de votre annonce"))
            {
                {
                    HtmlPasswordInput input1 = submitPage.getElementByName("passwd");
                    input1.setValueAttribute(DefaultUserConf.PASSWORD);
                }

                {
                    HtmlPasswordInput input2 = submitPage.getElementByName("passwd_ver");
                    input2.setValueAttribute(DefaultUserConf.PASSWORD);
                }

                {
                    final HtmlSubmitInput createButton = submitPage.getElementByName("create");
                    HtmlPage lastPage = createButton.click();

                    if (lastPage.asXml().contains("Un email de confirmation vient de vous être envoy"))
                    {
                        return true;
                    }
                }

            }
        }

        return false;

    }
}
