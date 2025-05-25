package modelo;

import java.util.HashSet;
import java.util.Set;

public class StopWords {
    private static Set<String> stopWords = new HashSet<>();

    static {
        // Lista de stop words em português
        String[] palavras = {
            "a", "o", "e", "é", "de", "do", "da", "em", "um", "uma", 
            "os", "as", "ao", "à", "para", "com", "não", "sim", 
            "que", "por", "se", "como", "mas", "ou", "nos", "nas", 
            "dos", "das", "na", "no", "pelo", "pela", "pelos", "pelas",
            "meu", "minha", "teu", "tua", "seu", "sua", "nossa", "nosso",
            "deles", "delas", "isto", "isso", "aquilo", "este", "esta",
            "esse", "essa", "aquele", "aquela", "estes", "estas", "esses",
            "essas", "aqueles", "aquelas", "outro", "outra", "todos",
            "todas", "alguém", "alguns", "algumas", "qualquer", "cada",
            "muito", "muita", "muitos", "muitas", "pouco", "pouca",
            "poucos", "poucas", "quanto", "quanta", "quantos", "quantas",
            "tanto", "tanta", "tantos", "tantas", "quem", "qual", "quais",
            "onde", "quando", "como", "porque", "porquê", "por que"
        };
        
        for (String palavra : palavras) {
            stopWords.add(palavra.toLowerCase());
        }
    }

    public static boolean isStopWord(String palavra) {
        return stopWords.contains(palavra.toLowerCase());
    }
}