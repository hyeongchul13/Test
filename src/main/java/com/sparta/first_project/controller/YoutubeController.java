//package com.sparta.first_project.controller;
//
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//import org.springframework.web.servlet.ModelAndView;
//
//public class YoutubeController {
//
//    @RequestMapping("/youtube/createAction.do")
//    public ModelAndView createAction(final MultipartHttpServletRequest multiRequest, @ModelAttribute("movieVO") MovieVO movieVO, BindingResult bindingResult,
//                                     Model model) throws Exception {
//
//        movieVO.setMv_the_origin_url("https://youtu.be/" + movieVO.getMv_the_origin_url());
//        movieService.youtubeInsert(movieVO);
//
//        ModelAndView mav = new ModelAndView(returnView + "bsite/youtube/create");
//        return mav;
//    }
//
//}
