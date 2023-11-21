package spring.backend.user_managment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManagmentService {

    @Autowired
    RestTemplate restTemplate;

    public String filterServer(String message, String loggedUser, String server) {
        String finalGoodMessage="";

        if (message != null) {
            List<String> goodMessages = Arrays.stream(message.split("@"))
                    .filter(part -> {
                        String[] elements = part.split("\\|");
                        return elements.length >= 2 && elements[1].equals(loggedUser);
                    })
                    .map(part -> {
                        String[] elements = part.split("\\|");
                        return String.join("|", elements);
                    })
                    .collect(Collectors.toList());


            finalGoodMessage = String.join("@", goodMessages);
            finalGoodMessage+="@";

            List<String> badMessages = Arrays.stream(message.split("@"))
                    .filter(part -> {
                        String[] elements = part.split("\\|");
                        return elements.length >= 2 && !elements[1].equals(loggedUser);
                    })
                    .map(part -> {
                        String[] elements = part.split("\\|");
                        return String.join("|", elements);
                    })
                    .collect(Collectors.toList());

            String finalBadMessage = String.join("@", badMessages);
            //String finalBadMessage =
               //     badMessages.stream()  // Convert the List to a Stream<String>
                 //           .filter(s -> !s.isEmpty())
                   //         .collect(Collectors.joining("@"));


            System.out.println("Gooood  "+ finalGoodMessage);
            System.out.println("Baaaad  "+ finalBadMessage);


            if(!finalBadMessage.isEmpty()) {
                HttpEntity<String> request = new HttpEntity<>(finalBadMessage+"@");
                ResponseEntity<String> response = restTemplate.postForEntity(server, request, String.class);
            }
        }


        return finalGoodMessage;
    }


    public StringBuilder filterMessage(String inputString)
    {
        // Split by "@" and then by "|"
        String[] dataRows = inputString.split("@");
        List<String[]> dataList = new ArrayList<>();
        for (String row : dataRows) {
            dataList.add(row.split("\\|"));
        }

        // Sort the data by the fifth element (index 4) and then by the third element (index 2) as strings
        dataList.sort(Comparator.comparing((String[] data) -> data[4])
                .thenComparing(data -> Integer.parseInt(data[2])));


        // Join the sorted data back with "|" separators and "@" separators between items
        StringBuilder sortedString = new StringBuilder();
        for (String[] data : dataList) {
            sortedString.append(String.join("|", data)).append("@");
        }

        // Remove the trailing "@" character
       if (sortedString.length() > 0) {
           sortedString.deleteCharAt(sortedString.length() - 1);
       }

        // Now sortedString contains the sorted data as a formatted string
        System.out.println("OVO JE KAO SORTIRANO: "+sortedString);

        return sortedString;
    }
}
