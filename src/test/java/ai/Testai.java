//package ai;
//
//import com.example.thehealingmeal.ai.CustomChatGPTService;
//import com.sun.tools.javac.Main;
//import io.github.flashvayne.chatgpt.dto.chat.MultiChatMessage;
//import io.github.flashvayne.chatgpt.service.ChatgptService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.DefaultResourceLoader;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.ResourcePatternUtils;
//import org.springframework.util.FileCopyUtils;
//import org.springframework.util.ResourceUtils;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
//import java.util.List;
//
//import static java.util.stream.Collectors.toList;
//
//
//public class Testai {
//
//    String htmlFileReader() throws IOException{
//
//        Resource[] resources = ResourcePatternUtils
//                .getResourcePatternResolver(new DefaultResourceLoader())
//                .getResources("classpath*:/resources/food-data.json");
//        return resources.toString();
//    }
//
////    @Test
////    public void test(){
////        String responseMessage =    chatgptService.multiChat(Arrays.asList(new MultiChatMessage("user","how are you?")));
////        System.out.print(responseMessage); //\n\nAs an AI language model, I don't have feelings, but I'm functioning well. Thank you for asking. How can I assist you today?
////    }
//
//    @Test
//    public void test1() throws IOException {
//        ClassPathResource cpr = new ClassPathResource("food-data.json");
//        byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
//        String data = new String(bdata, StandardCharsets.UTF_8);
//        System.out.println(data);
//    }
//}
