package org.brownmun.web.yourmun;

import com.google.common.collect.Lists;
import org.brownmun.model.advisor.Advisor;
import org.brownmun.model.repo.DelegatePosition;
import org.brownmun.model.repo.DelegateRepository;
import org.brownmun.web.security.AdvisorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping(value = {"/yourmun/delegates", "/yourmun/delegates/"})
public class DelegatesController
{
    private static final Logger log = LoggerFactory.getLogger(DelegatesController.class);

    private final AdvisorService advisorService;
    private final DelegateRepository delegateRepository;

    public DelegatesController(AdvisorService advisorService, DelegateRepository delegateRepository)
    {
        this.advisorService = advisorService;
        this.delegateRepository = delegateRepository;
    }

    @GetMapping
    public String delegates(@AuthenticationPrincipal Advisor advisor, Model model)
    {
        advisor = advisorService.load(advisor);
        DelegateNameForm form = new DelegateNameForm(delegateRepository.findDelegates(advisor.getSchool().getId()));
        model.addAttribute("delegateForm", form);

        return "yourmun/delegates";
    }

    @Transactional
    @PostMapping
    public String updateDelegateNames(@AuthenticationPrincipal Advisor advisor, @ModelAttribute DelegateNameForm form, Model model, RedirectAttributes redirect)
    {
        long schoolId = advisorService.load(advisor).getSchool().getId();
        for (DelegatePosition delegate : form.delegates)
        {
            int updated = delegateRepository.nameDelegate(delegate.getDelegateName(), delegate.getDelegateId(), schoolId);
            if (updated < 1)
            {
                log.warn("Failed to name delegate {} (for school {})", delegate.getDelegateId(), schoolId);
                model.addAttribute("error",
                        String.format("Could not update delegate for %s in %s", delegate.getPositionName(), delegate.getCommitteeName()));
                model.addAttribute("delegates", form.delegates);
                return "yourmun/delegates";
            }
        }

        redirect.addFlashAttribute("message", "Delegate names updated");
        return "redirect:/yourmun/";
    }



    public static class DelegateNameForm
    {
        private List<DelegatePosition> delegates;

        public DelegateNameForm(List<DelegatePosition> delegates)
        {
            this.delegates = delegates;
        }

        public DelegateNameForm()
        {
            this.delegates = Lists.newArrayList();
        }

        public List<DelegatePosition> getDelegates()
        {
            return delegates;
        }

        public void setDelegates(List<DelegatePosition> delegates)
        {
            this.delegates = delegates;
        }
    }
}
