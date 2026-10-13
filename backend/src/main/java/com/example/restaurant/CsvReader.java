package com.example.restaurant;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.StandardCharsets;
import java.util.*;
@Component class CsvReader {
 List<String[]> read(MultipartFile file){
  try {
   if(file==null||file.isEmpty()) throw ApiErrors.bad("CSV file is empty");
   String contents=new String(file.getBytes(),StandardCharsets.UTF_8).replace("\r","");
   String[] lines=contents.split("\n");
   if(lines.length<2)throw ApiErrors.bad("CSV requires a header and at least one row");
   List<String[]> result=new ArrayList<>();
   for(int i=1;i<lines.length;i++)if(!lines[i].isBlank())result.add(Arrays.stream(lines[i].split(",",-1)).map(String::trim).toArray(String[]::new));
   if(result.isEmpty())throw ApiErrors.bad("CSV contains no records");
   return result;
  } catch(org.springframework.web.server.ResponseStatusException ex){throw ex;}
    catch(Exception ex){throw ApiErrors.bad("Could not read CSV file");}
 }
}
