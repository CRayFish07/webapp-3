package org.literacyapp.web.content.letter;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.literacyapp.dao.ContentCreationEventDao;
import org.literacyapp.dao.LetterDao;
import org.literacyapp.model.Contributor;
import org.literacyapp.model.content.Letter;
import org.literacyapp.model.enums.Locale;
import org.literacyapp.model.contributor.ContentCreationEvent;
import org.literacyapp.model.enums.Environment;
import org.literacyapp.model.enums.Team;
import org.literacyapp.util.SlackApiHelper;
import org.literacyapp.web.context.EnvironmentContextLoaderListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/content/letter/list")
public class LetterListController {
    
    private final Logger logger = Logger.getLogger(getClass());
    
    @Autowired
    private LetterDao letterDao;
    
    @Autowired
    private ContentCreationEventDao contentCreationEventDao;

    @RequestMapping(method = RequestMethod.GET)
    public String handleRequest(Model model, HttpSession session) {
    	logger.info("handleRequest");
        
        Contributor contributor = (Contributor) session.getAttribute("contributor");
        
        // To ease development/testing, auto-generate Letters
        List<Letter> lettersGenerated = generateLetters(contributor.getLocale());
        for (Letter letter : lettersGenerated) {
            Letter existingLetter = letterDao.readByText(letter.getLocale(), letter.getText());
            if (existingLetter == null) {
                letterDao.create(letter);

                ContentCreationEvent contentCreationEvent = new ContentCreationEvent();
                contentCreationEvent.setContributor(contributor);
                contentCreationEvent.setContent(letter);
                contentCreationEvent.setCalendar(Calendar.getInstance());
                contentCreationEventDao.create(contentCreationEvent);
                
                if (EnvironmentContextLoaderListener.env == Environment.PROD) {
                    String text = URLEncoder.encode(
                            contributor.getFirstName() + " just added a new Letter:\n" + 
                            "• Language: \"" + letter.getLocale().getLanguage() + "\"\n" + 
                            "• Text: '" + letter.getText() + "'\n" + 
                            "See ") + "http://literacyapp.org/content/letter/list";
                    String iconUrl = contributor.getImageUrl();
                    SlackApiHelper.postMessage(Team.CONTENT_CREATION, text, iconUrl, null);
                }
            }
        }
        
        List<Letter> letters = letterDao.readAllOrdered(contributor.getLocale());
        model.addAttribute("letters", letters);

        return "content/letter/list";
    }
    
    private List<Letter> generateLetters(Locale locale) {
        List<Letter> letters = new ArrayList<>();
        
        List<String> letterStringArray = new ArrayList<>();
        if (locale == Locale.AR) {
            // TODO
        } else if (locale == Locale.EN) {
            letterStringArray = new ArrayList<>(Arrays.asList("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"));
        } else if (locale == Locale.ES) {
            letterStringArray = new ArrayList<>(Arrays.asList("a","b","c","d","e","f","g","h","i","j","k","l","m","n","ñ","o","p","q","r","s","t","u","v","w","x","y","z"));
        } else if (locale == Locale.SW) {
            letterStringArray = new ArrayList<>(Arrays.asList("a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","r","s","t","u","v","w","y","z"));
        }
        
        for (String letterString : letterStringArray) {
            Letter letter = new Letter();
            letter.setLocale(locale);
            letter.setRevisionNumber(1);
            letter.setTimeLastUpdate(Calendar.getInstance());
            letter.setText(letterString);
            letters.add(letter);
        }
        
        return letters;
    }
}