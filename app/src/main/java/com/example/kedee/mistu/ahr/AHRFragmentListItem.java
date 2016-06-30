package com.example.kedee.mistu.ahr;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class AHRFragmentListItem {
    private int helpReqId;
    private int helpieId;

    private int picId;
    private Bitmap pic;

    private String name;
    private String category;

    private String streamBranch;
    private String hrTitle;
    private String hrDetails;
    private String tag1;
    private String tag2;
    private String tag3;
    private boolean hasAccepted=false;
    private boolean hasPassed=false;


    public AHRFragmentListItem(String name, String streamBranch,String category, String hrTitle,
                               String hrDetails,int helpReqId,int userId,int picId,String tag1,String tag2, String tag3) {
        this.helpReqId = helpReqId;
        this.helpieId = userId;
        this.picId = picId;
        this.name = name;
        this.streamBranch = streamBranch;
        this.hrTitle = hrTitle;
        this.hrDetails = hrDetails;
        this.category=category;
        this.tag1=tag1;
        this.tag2=tag2;
        this.tag3=tag3;
    }

    public AHRFragmentListItem(String name, String streamBranch,String category, String hrTitle,
                               String hrDetails,int helpReqId,int userId,String tag1,String tag2, String tag3) {
        this.helpReqId = helpReqId;
        this.helpieId = userId;
        this.name = name;
        this.streamBranch = streamBranch;
        this.hrTitle = hrTitle;
        this.hrDetails = hrDetails;
        this.category=category;
        this.tag1=tag1;
        this.tag2=tag2;
        this.tag3=tag3;
       // new DownloadImage(helpieId).execute();
    }



    private class DownloadImage extends AsyncTask<Void, Void, Bitmap> {
        Bitmap image=null;
        int user_id;

        public DownloadImage(int user_id ) {
            this.user_id=user_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            String url_pic="http://www.mistu.org/email/getimage.php?id="+user_id;
            URL url = null;
            try {
                url = new URL(url_pic);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            super.onPostExecute(b);
            pic=b;
            Log.i("PIC",pic.toString());
            //Toast.makeText(getApplicationContext(),"Inside on Post Execute",Toast.LENGTH_LONG).show();
        }
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getHelpReqId() {
        return helpReqId;
    }

    public void setHelpReqId(int helpReqId) {
        this.helpReqId = helpReqId;
    }

    public int getHelpieId() {
        return helpieId;
    }

    public void setHelpieId(int userId) {
        this.helpieId = userId;
    }

    public String getHrDetails() {
        return hrDetails;
    }

    public void setHrDetails(String hrDetails) {
        this.hrDetails = hrDetails;
    }

    public String getName() {
        return name;
    }

    public int getPicId() {
        return picId;
    }

    public String getStreamBranch() {
        return streamBranch;
    }

    public boolean isHasAccepted() {
        return hasAccepted;
    }

    public String getHrTitle() {
        return hrTitle;
    }

    public boolean isHasPassed() {
        return hasPassed;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStreamBranch(String streamBranch) {
        this.streamBranch = streamBranch;
    }

    public void setHrTitle(String hrTitle) {
        this.hrTitle = hrTitle;
    }

    public void setHasAccepted(boolean hasAccepted) {
        this.hasAccepted = hasAccepted;
    }

    public void setHasPassed(boolean hasPassed) {
        this.hasPassed = hasPassed;
    }
}
