package com.akp.savarn.chatreport;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Collection;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/*
import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
*/
public class ChatBot
{
    private TreeMap<String, String> quesResponseMap=null;
    private TreeMap<String, String> localQuesResponseMap=null;
    private Set<String> questions=null;
    private Collection<String> answers=null;
    private String botAnswer="";
    private String chatHistory="";
    private String username="";

    //static variables
   // private static TextSpeech textSpeech;
    public static Boolean readyToLearnSomethingNew = false;
    public static String question = "";

    //COsntants
    private static final String BOT_INTRO="Hi! I am your Assistant. How can I help you?";
    private static final String SORRY_RESPONSE = "Sorry! I did't get you.";
    private static final String SYSTEM_ERROR ="Sorry! there is an error in my system.";
    private static final String LEARN_REQUEST ="WHat should be my answer to this statement?";
    private static final String EXIT ="BYE! Have a nice day.";
    public static final String DATABASE_NAME ="KnowledgeBase";
    public static String DATABASE ="KnowledgeBase";
    private static final String BOT_NAME="Bot : ";

    public static String getBotIntro() {
        return BOT_INTRO;
    }

    public static String getSorryResponse() {
        return SORRY_RESPONSE;
    }

    public static String getSystemError() {
        return SYSTEM_ERROR;
    }

    public static String getLearnRequest() {
        return LEARN_REQUEST;
    }

    public static String getEXIT() {
        return EXIT;
    }

    public static String getDATABASE() {
        return DATABASE;
    }

    public static String getBotName() {
        return BOT_NAME;
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public ChatBot() throws Exception
    {
        this.quesResponseMap= KnowledgeBase.getKnowledgeBase(ChatBot.DATABASE);
        System.out.println("========="+this.quesResponseMap);
        this.localQuesResponseMap = new TreeMap<String, String>();
        if(this.quesResponseMap == null)
            this.botAnswer= ChatBot.SORRY_RESPONSE ;
        else
        {
            questions = quesResponseMap.keySet();
            answers = quesResponseMap.values();
        }
       /* this.textSpeech = TextSpeech.getInstance();
        this.textSpeech.start();*/
    }

    public String saveQuestionAndResponse(String question , String response)
    {
        if(!question.trim().isEmpty() && !response.trim().isEmpty()) {
            this.quesResponseMap.put(question,response);
            this.localQuesResponseMap.put(question,response);
        }
        this.botAnswer="Okey. Got it!";
        return this.botAnswer;
    }

    public String getResponse(String question)
    {
        double globalRank=0.400;
        String localQuestion="";
        try{
            for(String ques:questions)
            {
                double localRank= StringSimilarity.similarity(ques,question);

                if(globalRank <localRank)
                {
                    globalRank = localRank;
                    localQuestion=ques;
                }
            }

            if(localQuestion.trim()!=""){
                this.botAnswer=quesResponseMap.get(localQuestion);
                if(this.botAnswer.trim().toLowerCase().contains("timedate")) {this.botAnswer = new Date().toString();}
            }
            else {
                this.botAnswer = ChatBot.SORRY_RESPONSE + " " + ChatBot.LEARN_REQUEST;
                readyToLearnSomethingNew = true;
                ChatBot.question = question;
            }
        }catch(Exception e)
        {
            this.botAnswer= ChatBot.SYSTEM_ERROR;
        }
        return this.botAnswer;
    }


    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public void checkExit(String exitStmt) throws Exception
    {
        if(exitStmt.toLowerCase().contains("exit"))
        {
            this.chatHistory += ChatBot.BOT_NAME+ ChatBot.EXIT;
            log(ChatBot.BOT_NAME+ ChatBot.EXIT );
            speak(ChatBot.EXIT);
            KnowledgeBase.saveNewKnowledge(this.localQuesResponseMap, ChatBot.DATABASE);
            KnowledgeBase.saveLogs(this.chatHistory,this.username);
         //   this.textSpeech.stop();

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public static void main(String arg[]) throws Exception {
        Scanner sc= new Scanner(System.in);
        ChatBot cb= new ChatBot();
        log(ChatBot.BOT_INTRO);
        speak(ChatBot.BOT_INTRO);
        cb.username=sc.nextLine();
        cb.chatHistory += "\n\n"+ ChatBot.BOT_NAME+"Hi "+cb.username+". How can I help you?\r\n";
        log("\n\n"+ ChatBot.BOT_NAME+"Hi "+cb.username+". How can I help you?");
        speak("Hi "+cb.username+". How can I help you?");
        while(true)
        {
            System.out.print(cb.username+" : ");
            String question = sc.nextLine();
            cb.chatHistory += cb.username+" : "+question+"\r\n";
            cb.checkExit(question.trim());
            String response = cb.getResponse(question);
            cb.chatHistory += ChatBot.BOT_NAME+response+"\r\n";
            log(ChatBot.BOT_NAME+response);
            speak(response.trim());
            if(response.toLowerCase().contains("sorry"))
            {
                log(cb.username+" : ");
                response = sc.nextLine();
                cb.chatHistory += cb.username+" : "+response;
                cb.checkExit(response.trim());
                cb.chatHistory += ChatBot.BOT_NAME+cb.saveQuestionAndResponse(question,response)+"\r\n";
                String res = cb.saveQuestionAndResponse(question,response);
                log(ChatBot.BOT_NAME+res);
                speak(res);
            }

        }
    }

    private static void log(String content){
        System.out.println(content);
    }

    public static void speak(String content)
    {
        try{
       //     textSpeech.speak(content);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


}