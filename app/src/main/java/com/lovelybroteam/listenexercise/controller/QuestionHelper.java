package com.lovelybroteam.listenexercise.controller;

import java.util.ArrayList;

/**
 * Created by Vo Quang Hoa on 12/22/2015.
 */
public class QuestionHelper {

    public static String convertToColor(String originalString, String colorCode){
        String clearColor = originalString.replace("color='red'", "color='" + colorCode + "'");
        String result = "<font color='"+colorCode+"'>" + clearColor +"</font>";
        return result;
    }

    private static String getQuestionString(ArrayList<String> lineGroup, int lineIndex){
        String removeRegex = "^(\\d+)(\\.|\\)) ";

        if(lineIndex < lineGroup.size()) {
            String question = lineGroup.get(lineIndex);
            if(question.startsWith("\uFEFF")){
                question = question.substring(1);
            }

            return question.replaceAll(removeRegex, "").replaceAll("\r", "");
        }
        return "";
    }

    private static String getAnswerString(ArrayList<String> lineGroup, int lineIndex){
        if(lineIndex < lineGroup.size()) {
            String answer = lineGroup.get(lineIndex);

            if(answer.length()>0){
                char firstChar = answer.charAt(0);

                if(firstChar=='('){
                    if(answer.charAt(2)==')' && answer.length()>=5) {
                        answer = answer.substring(4);
                    }
                }else if(isCharBetweenValue(firstChar, 'A', 'D') && answer.length()>=3){
                    if(isCharInSet(answer.charAt(1), '.', ')')){
                        answer=answer.substring(3);
                    }
                }else if(answer.length()>=4 && isCharBetweenValue(answer.charAt(1), 'A', 'D')){
                    if(answer.charAt(2)=='.' && answer.charAt(0)==' '){
                        answer=answer.substring(4);
                    }
                }
            }

            return answer.replaceAll("\r","");
        }
        return "";
    }

    private static boolean isCharInSet(char ch, char ... set){
        for(int i=0; i<set.length; i++){
            if(ch == set[i]){
                return true;
            }
        }
        return false;
    }

    private static boolean isCharBetweenValue(char ch, char minimal, char maximum){
        ch = Character.toUpperCase(ch);
        minimal = Character.toUpperCase(minimal);
        maximum = Character.toUpperCase(maximum);
        return ch >= minimal && ch <= maximum;
    }

    private static ArrayList<ArrayList<String>> analystLines(ArrayList<String> lines){
        int firstLine = 0;
        ArrayList<ArrayList<String>> lineGroup = new ArrayList<ArrayList<String>>();
        while(firstLine < lines.size()){
            ArrayList<String> group = new ArrayList<>();

            while(firstLine < lines.size() && lines.get(firstLine).length()==0) {
                firstLine++;
            }

            while(firstLine < lines.size() && lines.get(firstLine).length()>0) {
                group.add(lines.get(firstLine));
                firstLine++;
            }
            if(group.size()>0){
                lineGroup.add(group);
            }
        }
        return lineGroup;
    }
}
