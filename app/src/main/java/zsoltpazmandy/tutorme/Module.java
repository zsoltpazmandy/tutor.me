package zsoltpazmandy.tutorme;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zsolt Pazmandy on 18/08/16.
 * MSc Computer Science - University of Birmingham
 * zxp590@student.bham.ac.uk
 *
 * Used to create module POJOs & perform some module functions.
 */
class Module {

    private String name;
    private String description;
    private int pro;
    private String author;
    private int noOfSlides;
    private String ID;

    public Module() {
    }

    public Module(String name,
                  String description,
                  int pro,
                  String author,
                  ArrayList<Integer> reviews,
                  ArrayList<Integer> trainers,
                  ArrayList<Integer> typesOfSlides,
                  int noOfSlides,
                  String ID) {

        this.name = name;
        this.description = description;
        this.pro = pro;
        this.author = author;
        ArrayList<Integer> reviews1 = reviews;
        ArrayList<Integer> trainers1 = trainers;
        ArrayList<Integer> typesOfSlides1 = typesOfSlides;
        this.noOfSlides = noOfSlides;
        this.ID = ID;

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoOfSlides() {
        return noOfSlides;
    }

    public void setNoOfSlides(int noOfSlides) {
        this.noOfSlides = noOfSlides;
    }

    public int getPro() {
        return pro;
    }

    public void setPro(int pro) {
        this.pro = pro;
    }

    /**
     * Removes the slide from the module at the index provided as an argument
     *
     * @param moduleMap the Module from which a slide is being removed
     * @param indexOfSlideToRemove the index at which a slide is to be removed from the module
     * @return
     */
    public HashMap<String, Object> removeSlide(HashMap<String, Object> moduleMap, int indexOfSlideToRemove) {

        ArrayList<String> allSlides = new ArrayList<>();
        ArrayList<Integer> allTypes = new ArrayList<>();

        int slideCount = Integer.parseInt(moduleMap.get("noOfSlides").toString());

        HashMap<String, String> typesMap = (HashMap<String, String>) moduleMap.get("typesOfSlides");

        for (int i = 1; i <= slideCount; i++) {
            allSlides.add(moduleMap.get("Slide_" + i).toString());
            allTypes.add(Integer.parseInt(typesMap.get("Slide_"+i)));
        }

        for (int i = 1; i <= slideCount; i++) {
            moduleMap.remove("Slide_" + i);
        }
        moduleMap.remove("typesOfSlides");

        HashMap<String, String> newTypesMap = new HashMap<>();

        allSlides.remove(indexOfSlideToRemove);
        allTypes.remove(indexOfSlideToRemove);

        for (int i = 1; i <= slideCount; i++) {
            moduleMap.remove("Slide_" + i);
        }

        for (int i = 1; i <= slideCount - 1; i++) {
            moduleMap.put("Slide_" + i, allSlides.get(i - 1));
            newTypesMap.put("Slide_"+i, allTypes.get(i-1).toString());
        }

        moduleMap.put("typesOfSlides",newTypesMap);

        slideCount--;
        moduleMap.remove("noOfSlides");
        moduleMap.put("noOfSlides", ""+slideCount);

        return  moduleMap;
    }

    /**
     * Returns the type of the slide found at the index provided as an argument in the module
     * also provided as an argument
     *
     * @param module the module whose slide's type is being requested
     * @param slideNumber the index of the slide in the module whose type is being requested
     * @return
     */
    public int getSlideType(HashMap<String,Object> module, int slideNumber) {

        int slideType = 0;

        if (slideNumber > 0) {
            slideNumber--;
        }

        ArrayList<String> typesOfSlides = (ArrayList<String>) module.get("typesOfSlides");

        slideType = Integer.parseInt(typesOfSlides.get(slideNumber));

        return slideType;

    }

}
