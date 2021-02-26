package com.example.appsecurity;

import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tom_roush.pdfbox.cos.COSDocument;
import com.tom_roush.pdfbox.io.RandomAccessFile;
import com.tom_roush.pdfbox.pdfparser.PDFParser;
import com.tom_roush.pdfbox.pdmodel.PDDocument;
import com.tom_roush.pdfbox.text.PDFTextStripper;
import com.tom_roush.pdfbox.util.PDFBoxResourceLoader;

import java.io.File;
import java.io.IOException;

public class ExtractMainActivity extends AppCompatActivity {
    TextView name_file;
    TextView yes_no;
    TextView result;
    int position=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extractmain);
        name_file=(TextView)findViewById(R.id.textView11);
        yes_no=(TextView)findViewById(R.id.textView12);
        result=(TextView)findViewById(R.id.textView14);
        position=getIntent().getIntExtra("position",-1);
        String name_file1=ExtractFileActivity.fileList.get(position).getName();
        name_file.setText(name_file1);
        File file_1=new File(Environment.getExternalStorageDirectory() + "/.AppSecurity/" + name_file1);
        String hash_extract="";
        PDFBoxResourceLoader.init(getApplicationContext());
        String [] Original_ru={"2d","3b","410","412","415","41a","41c","41d","41e","420","421","422","425","435","43e","441","445"};
        String [] Duplicate_ru={"2010","37e","41","42","45","4b","4d","48","4f","50","43","54","58","65","6f","63","78"};
        String [] Spaces={"20","2000","2004","2005","2008","2009","202f","205f"};
        String [] bit_spaces={"000","001","010","011","100","101","110","111"};
        String [] Confusables_ru={"2d","3b","410","412","415","41a","41c","41d","41e","420","421","422","425","435","43e","441","445","2010","37e","41","42","45","4b","4d","48","4f","50","43","54","58","65","6f","63","78","20","2000","2004","2005","2008","2009","202f","205f"};
        int size_Con_ru=Confusables_ru.length;
        int br=0;
        try{
            RandomAccessFile randomAccessFile=new RandomAccessFile(file_1,"r");
            PDFParser pdfParser=new PDFParser(randomAccessFile);
            pdfParser.parse();
            COSDocument cosDocument=pdfParser.getDocument();
            PDFTextStripper pdfTextStripper=new PDFTextStripper();
            PDDocument doccc=new PDDocument(cosDocument);
            doccc.getClass();
            int coll_str=doccc.getNumberOfPages();
            for(int i9=0;i9<coll_str;i9++) {
                pdfTextStripper.setStartPage(i9);
                pdfTextStripper.setEndPage(i9 + 1);
                String Text_pdf = pdfTextStripper.getText(doccc);
                int len_text_ru = Text_pdf.length();
                String text_ru = Text_pdf;
                Character[] bits_ru = new Character[3];
                for (int i = 0; i < len_text_ru; i++) {
                    char aa = text_ru.charAt(i);
                    String symb_en = Integer.toHexString(aa);
                    int con_otvet = 0;
                    int index_c = 0;
                    for (int j = 0; j < 34; j++) {
                        int pr_1 = 0;
                        int l_1 = 0;
                        String pr = Confusables_ru[j];
                        if (symb_en.length() == pr.length()) {
                            l_1 = symb_en.length();
                            for (int j6 = 0; j6 < symb_en.length(); j6++) {
                                if (symb_en.charAt(j6) == pr.charAt(j6)) {
                                    pr_1++;
                                }
                            }
                            if (pr_1 == l_1) {
                                con_otvet++;
                                index_c = j;
                            }
                        }
                    }
                    if (con_otvet == 1) {
                        if(index_c>16) {
                           hash_extract+="1";
                        }
                        if(index_c<17) {
                            hash_extract+="0";
                        }
                    }
                }
                int Pr=0;
                for(int ij=0; ij<hash_extract.length();ij++){
                    if(hash_extract.charAt(ij)=='1'){
                        Pr++;
                    }
                    if(Pr<20){
                        yes_no.setText("не содержит");
                    }
                    if((Pr>19)&&(hash_extract.length()>69)){
                        yes_no.setText("содержит");
                        br=1;
                    }
                    if(hash_extract.length()<70){
                        yes_no.setText("не содержит");
                    }
                }
                if(br==1){
                    break;
                }
            }
            doccc.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
