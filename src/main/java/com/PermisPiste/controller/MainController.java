package com.PermisPiste.controller;

import com.PermisPiste.entity.Action;
import com.PermisPiste.entity.Learner;
import com.PermisPiste.entity.Mission;
import com.PermisPiste.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private IndicatorRepository indicatorRepository;
    @Autowired
    private InscriptionRepository inscriptionRepository;
    @Autowired
    private LearnerRepository learnerRepository;
    @Autowired
    private MissionRepository missionRepository;
/*
    @GetMapping("/actions")
    @ResponseBody
    public List<Action> showAllActions(){
        return actionRepository.findAll();
    }

    @GetMapping("/action/{id}")
    @ResponseBody
    public Action showActionById(@PathVariable Integer id){
        return actionRepository.findById(id).orElse(null);
    }

    @GetMapping("/action/{wording}/{scoreMinimum}")
    @ResponseBody
    public Action createAction(@PathVariable String wording, @PathVariable Integer scoreMinimum){
        Action action = new Action();
        action.setWording(wording);
        action.setScoreMinimum(scoreMinimum);
        return actionRepository.save(action);
    }*/

    @GetMapping("/")
    public String showIndex(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "index";
    }

    // region [- Apprenant -]

    @GetMapping("/apprenant/liste")
    public String getListApprenant(Model model) throws Exception {
        model.addAttribute("apprenants",learnerRepository.findAll());
        return "apprenant/liste";
    }

    @GetMapping("/apprenant/{id}")
    public String GetApprenant(@PathVariable("id") Integer id, Model model) throws Exception {
        Optional<Learner> learner = learnerRepository.findById(id);
        if(learner.isPresent()) {
            model.addAttribute("learner",learner.get());
            model.addAttribute("inscriptions",inscriptionRepository.getInscriptionOfLearner(id));
        }
        else{
            model.addAttribute("error","Apprennant introuvable");
        }
        return "apprenant/voir";
    }

    @GetMapping("/apprenant/update/{id}")
    public String GetApprenantUpdate(@PathVariable("id") Integer id, Model model) throws Exception {
        model.addAttribute("updating",true);
        return GetApprenant(id, model);
    }

    @GetMapping("/apprenant/create/{surname}/{forname}/{salt}/{email}/{mdp}/{role}")
    public String CreateApprenant(@PathVariable("surname") String surname,
                                  @PathVariable("forname") String forname,
                                  @PathVariable("salt") String salt,
                                  @PathVariable("email") String email,
                                  @PathVariable("mdp") String mdp,
                                  @PathVariable("role") String role, Model model){
        Learner learner = new Learner();
        learner.setSurname(surname);
        learner.setForname(forname);
        learner.setSalt(salt);
        learner.setEmail(email);
        learner.setMdp(mdp);


        model.addAttribute("result",learnerRepository.save(learner));

        return "apprenant/created";
    }

    @GetMapping("/apprenant/delete/{id}")
    public String DeleteApprenant(@PathVariable("id") Integer id, Model model){
        learnerRepository.deleteById(id);

        model.addAttribute("result", !learnerRepository.findById(id).isPresent());

        return "apprenant/deleted";
    }

    @GetMapping("/apprenant/{id}/update/{surname}/{forname}/{salt}/{email}/{mdp}/{role}")
    public String UpdateApprenant(@PathVariable("id") Integer id,
                                  @PathVariable("surname") String surname,
                                  @PathVariable("forname") String forname,
                                  @PathVariable("salt") String salt,
                                  @PathVariable("email") String email,
                                  @PathVariable("mdp") String mdp,
                                  @PathVariable("role") String role, Model model){
        Optional<Learner> optionalLearner = learnerRepository.findById(id);
        if(optionalLearner.isPresent()) {
            Learner learner = optionalLearner.get();
            learner.setSurname(surname);
            learner.setForname(forname);
            learner.setSalt(salt);
            learner.setEmail(email);
            learner.setMdp(mdp);

            model.addAttribute("result", learnerRepository.save(learner));
        }
        else{
            model.addAttribute("error","Apprennant introuvable");
        }



        return "apprenant/updated";
    }

    // endregion Apprennant

    // region [- Mission -]
    @GetMapping("/mission/liste")
    public String GetListMission(Model model) throws Exception {
        model.addAttribute("missions", missionRepository.findAll());
        return "mission/liste";
    }

    @GetMapping("/mission/{id}")
    public String GetMission(@PathVariable("id") Integer id, Model model) throws Exception {
        Optional<Mission> mission = missionRepository.findById(id);
        if(mission.isPresent()) {
            model.addAttribute("mission",mission.get());
            model.addAttribute("actions",actionRepository.getActionOfMission(id));
        }
        else{
            model.addAttribute("error","Mission introuvable");
        }
        return "mission/voir";
    }

    @GetMapping("/mission/{id}/update")
    public String GetMissionUpdate(@PathVariable("id") Integer id, Model model) throws Exception {
        model.addAttribute("updating",true);
        return GetMission(id, model);
    }

    @GetMapping("/createMission/{wording}")
    public String CreateMission(@PathVariable("wording") String wording, Model model){
        Mission mission = new Mission();
        mission.setWording(wording);

        model.addAttribute("result",missionRepository.save(mission));

        return "mission/created";
    }

    @GetMapping("/deleteMission/{id}")
    public String DeleteMission(@PathVariable("id") Integer id, Model model){
        missionRepository.deleteById(id);

        model.addAttribute("result", !missionRepository.findById(id).isPresent());

        return "mission/deleted";
    }

    @GetMapping("/updateMission/{id}/{wording}")
    public String UpdateMission(@PathVariable("id") Integer id,
                                @PathVariable("wording") String wording, Model model){
        Optional<Mission> optionalMission = missionRepository.findById(id);
        if(optionalMission.isPresent()) {
            Mission mission = optionalMission.get();
            mission.setWording(wording);

            model.addAttribute("result", missionRepository.save(mission));
        }
        else{
            model.addAttribute("error","Mission introuvable");
        }

        return "mission/updated";
    }

    // endregion [- Mission -]

    // region [- Action -]
    @GetMapping("/action")
    public String GetListAction(Model model) throws Exception {
        model.addAttribute("actions", actionRepository.findAll());
        return "action/liste";
    }

    @GetMapping("/action/{id}")
    public String GetAction(@PathVariable("id") Integer id, Model model) throws Exception {
        Optional<Action> action = actionRepository.findById(id);
        if(action.isPresent()) {
            model.addAttribute("action",action.get());
            model.addAttribute("indicators",indicatorRepository.getIndicatorsOfAction(id));
        }
        else{
            model.addAttribute("error","Action introuvable");
        }
        return "action/voir";
    }

    @GetMapping("/action/{id}/update")
    public String GetActionUpdate(@PathVariable("id") Integer id, Model model) throws Exception {
        model.addAttribute("updating",true);
        return GetAction(id, model);
    }

    @GetMapping("/action/create/{wording}/{scoreMinimum}")
    public String CreateAction(@PathVariable("wording") String wording,
                               @PathVariable("scoreMinimum") Integer scoreMinimum,
                               Model model){
        Action action = new Action();
        action.setWording(wording);
        action.setScoreMinimum(scoreMinimum);

        model.addAttribute("result",actionRepository.save(action));

        return "action/created";
    }

    @GetMapping("/action/{id}/delete")
    public String DeleteAction(@PathVariable("id") Integer id, Model model){
        actionRepository.deleteById(id);

        model.addAttribute("result", !actionRepository.findById(id).isPresent());

        return "action/deleted";
    }

    @GetMapping("/action/{id}/update/{wording}/{scoreMinimum}")
    public String UpdateAction(@PathVariable("id") Integer id,
                               @PathVariable("wording") String wording,
                               @PathVariable("scoreMinimum") Integer scoreMinimum,
                               Model model){
        Optional<Action> optionalAction = actionRepository.findById(id);
        if(optionalAction.isPresent()) {
            Action action = optionalAction.get();
            action.setWording(wording);
            action.setScoreMinimum(scoreMinimum);

            model.addAttribute("result", actionRepository.save(action));
        }
        else{
            model.addAttribute("error","Action introuvable");
        }

        return "action/updated";
    }

    // endregion [- Actions -]


}
