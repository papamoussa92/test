package fr.sis.sisid.copuk.cop.core.rules;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.util.List;


//Created by Álvaro López-Müller 2019.

public class SimpleNameMatcher {


    public static boolean isNameMatchingAlgo(String ref, String term, int occur){
        if(term.isEmpty()) return false;
        if(term.equals(ref)) return true;
        if(ref.toLowerCase().startsWith(term.toLowerCase())) return true;
        if(ref.toLowerCase().endsWith(term.toLowerCase())) return true;
        if(ref.toLowerCase().contains(term.toLowerCase())) return true;
        List<String> words = List.of(term.split(" "));
        List<String> refWords = List.of(ref.trim().split(" "));
        if(words.size() < 2) return false;
        for (String word : words){
            if(isNameMatchingAlgo(ref, word, occur)) {
                occur = occur + 1;
            };
        }
        float overrage = (occur / refWords.size());
        return Float.compare(overrage, 1f) == 0;
    }
    public static String cleanData(String word){
        return word.replace("-"," ")
                .replace("_"," ")
                .replace("."," ")
                .replace(":"," ")
                .replace("."," ")
                .replace("/e/g", "[e,è,é,ê,ë]")
                .replace("/a/g", "[a,à,á,â,ã,ä,å]")
                .replace("/b/g", "[b,þ]")
                .replace("/c/", "[c,¢,Ç]")
                .replace("/f/g", "[f,ƒ]")
                .replace("/i/g", "[i,ì,î,í,ï]")
                .replace("/n/g", "[n,ñ]")
                .replace("/o/g", "[o,ð,ò,ó,ô,õ,ö,ø]")
                .replace("/s/g", "[s,š]")
                .replace("/u/g", "[u,µ,ù,ú,û,ü]")
                .replace("/y/g", "[y,ý]")
                .replace("/z/g", "[z,ž]");
    }

}
