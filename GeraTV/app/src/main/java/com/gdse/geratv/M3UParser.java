package com.gdse.geratv;

import android.content.Context;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class M3UParser {
    /*
    #EXTINF:-1 tvg-id="2x2.ru" tvg-logo="https://i.imgur.com/fhQFLEl.png" group-title="Entertainment",2x2 (720p)
    https://bl.uma.media/live/317805/HLS/4614144_3/2/1/playlist.m3u8
     */
    private final String EXT_INF_SP = "#EXTINF:";
    //private final String KOD_IP_DROP_TYPE = "#KODIPROP:inputstream.adaptive.license_type=";
    //private final String KOD_IP_DROP_KEY = "#KODIPROP:inputstream.adaptive.license_key=";
    private final String TVG_NAME = "tvg-name=";
    private final String TVG_LOGO = "tvg-logo=";
    private final String GROUP_TITLE = "group-title=";
    private final String COMMA = ",";
    private final String HTTP = "http://";
    private final String HTTPS = "https://";


    private Context ctx;


    public M3UParser(Context context) {
        ctx = context;
    }

    List<Stream> parse(String filepath) {
        List<Stream> streamList = null;

        InputStream inputStreamReader = null;
        try {
            //inputStreamReader = ctx.getContentResolver().openInputStream(Uri.parse(filepath));
            inputStreamReader = ctx.getAssets().open(filepath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamReader));

            String currentLine;
            Stream stream = null;

            while ((currentLine = bufferedReader.readLine()) != null) {
                currentLine = currentLine.replaceAll("\"", "");

                if (currentLine.startsWith(EXT_INF_SP)) {
                    stream = new Stream();

                    if(currentLine.contains(TVG_NAME) || currentLine.contains(COMMA)) {

                        stream.setName(currentLine.split(TVG_NAME).length > 1 ?
                                currentLine.split(TVG_NAME)[1].split(TVG_LOGO)[0] :
                                currentLine.split(COMMA)[1]);

                        if(currentLine.contains(GROUP_TITLE)) stream.setGroup(currentLine.split(GROUP_TITLE)[1].split(COMMA)[0]);
                        else stream.setGroup("");
                        if(currentLine.contains(TVG_LOGO)) stream.setLogo(currentLine.split(TVG_LOGO).length > 1 ?
                                currentLine.split(TVG_LOGO)[1].split(GROUP_TITLE)[0] : "");
                        else stream.setLogo("");
                    } else {
                        stream.setName("--");
                    }
                    //continue;
                } else if (stream != null && (currentLine.startsWith(HTTP) || currentLine.startsWith(HTTPS)) ) {
                    stream.setUrl(currentLine);

                    if(streamList == null) streamList = new ArrayList<Stream>();
                    streamList.add(stream);

                    stream = null;
                } else {
                    stream = null;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return streamList;
    }
}
