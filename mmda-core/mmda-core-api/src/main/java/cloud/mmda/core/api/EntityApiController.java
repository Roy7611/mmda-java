package cloud.mmda.core.api;

import cloud.mmda.core.entities.Entity;
import cloud.mmda.core.security.models.UserAccount;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

public class EntityApiController<T extends Entity, K> {
    //region security

//    @ModelAttribute
//    public UserAccount populateUserAccount(Model model, Principal principal) {
//
//        model.addAttribute("userAccount", new UserAccount());
//    }
    //endregion
}
