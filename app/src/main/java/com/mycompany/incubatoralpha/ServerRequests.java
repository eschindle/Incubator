package com.mycompany.incubatoralpha;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class ServerRequests {

    ProgressDialog progressDialog;
    public static final String SERVER_ADDRESS = "http://cgi.soic.indiana.edu/~team41/";

    public ServerRequests(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    private String getEncodedData(Map<String, String> data) {
        StringBuilder sb = new StringBuilder();
        for (String key : data.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(data.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (sb.length() > 0)
                sb.append("&");

            sb.append(key).append("=").append(value);
        }
        return sb.toString();
    }

    public void storeUserDataInBackground(User user, GetUserCallback userCallback) {
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallback).execute();
    }

    public void fetchUserDataInBackground(User user, GetUserCallback callback) {
        progressDialog.show();
        new fetchUserDataAsyncTask(user, callback).execute();
    }

    public void fetchUserAttributesInBackground(User user, GetUserCallback callback) {
        progressDialog.show();
        new fetchUserAttributesAsyncTask(user, callback).execute();
    }

    public void fetchUserIdeasInBackground(User user, GetUserCallback userCallback) {
        progressDialog.show();
        new fetchUserIdeaAsyncTask(user, userCallback).execute();
    }

    public void fetchOtherUserIdeasInBackground(User user, GetUserCallback userCallback) {
        progressDialog.show();
        new fetchOtherUserIdeaAsyncTask(user, userCallback).execute();
    }

    public void storeUserEducationInBackground(User user, GetUserCallback userCallback) {
        progressDialog.show();
        new StoreUserEducationAsyncTask(user, userCallback).execute();
    }

    public void storeUserWorkInBackground(User user, GetUserCallback userCallback) {
        progressDialog.show();
        new StoreUserWorkAsyncTask(user, userCallback).execute();
    }

    public void storeUserSkillInBackground(User user, GetUserCallback userCallback) {
        progressDialog.show();
        new StoreUserSkillAsyncTask(user, userCallback).execute();
    }

    public void storeIdeaInBackground(Idea idea, GetIdeaCallback ideaCallback){
        progressDialog.show();
        new StoreIdeaAsyncTask(idea, ideaCallback).execute();
    }

    public void deleteUserEducationInBackground(User user, GetUserCallback userCallback) {
        progressDialog.show();
        new DeleteUserEducationAsyncTask(user, userCallback).execute();
    }

    public void deleteUserSkillInBackground(User user, GetUserCallback userCallback) {
        progressDialog.show();
        new DeleteUserSkillAsyncTask(user, userCallback).execute();
    }

    public void fetchUserConnectInBackground(User user, GetUserCallback userCallback) {
        progressDialog.show();
        new fetchUserConnectAsyncTask(user, userCallback).execute();
    }

    public void updateUserConnectInBackground(User user, GetUserCallback userCallback) {
        progressDialog.show();
        new updateUserConnectAsyncTask(user, userCallback).execute();
    }

    public void fetchContactsInBackground(User user, GetUserCallback userCallback) {
        progressDialog.show();
        new fetchContactsAsyncTask(user, userCallback).execute();
    }

    public class fetchContactsAsyncTask extends AsyncTask<Void, Void, String>{

        User user;
        GetUserCallback userCallback;

        public fetchContactsAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder sb = new StringBuilder();
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("uid", user.uid + "");

            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader;
            try {
                URL url = new URL(SERVER_ADDRESS + "fetchContacts.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(encodedStr);
                writer.flush();
                writer.close();
                os.close();

                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                line = sb.toString();

                Log.i("custom_check", "The values received in the store part are as follows:");
                Log.i("custom_check", line);

                con.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            userCallback.donezo(s);
            super.onPostExecute(s);
        }
    }

    public class updateUserConnectAsyncTask extends AsyncTask<Void, Void, Void>{

        User user;
        GetUserCallback userCallback;

        public updateUserConnectAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("uid",user.uid+"");
            dataToSend.put("ouid", user.ouid+"");
            dataToSend.put("like", user.like+"");

            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader = null;

            try {
                URL url = new URL(SERVER_ADDRESS + "updateConnect.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(encodedStr);
                writer.flush();
                writer.close();
                os.close();

                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                line = sb.toString();

                Log.i("custom_check", "The values received in the store part are as follows:");
                Log.i("custom_check", line);

                con.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }

    public class fetchUserConnectAsyncTask extends AsyncTask<Void, Void, String>{

        User user;
        GetUserCallback userCallback;

        public fetchUserConnectAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder sb = new StringBuilder();
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("uid", user.uid + "");

            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader;
            try {
                URL url = new URL(SERVER_ADDRESS + "fetchUserConnect.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(encodedStr);
                writer.flush();
                writer.close();
                os.close();

                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                line = sb.toString();

                Log.i("custom_check", "The values received in the store part are as follows:");
                Log.i("custom_check", line);

                con.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            userCallback.donezo(s);
            super.onPostExecute(s);
        }
    }

    public class DeleteUserSkillAsyncTask extends AsyncTask<Void, Void, Void>{

        User user;
        GetUserCallback userCallback;

        public DeleteUserSkillAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("uid",user.uid+"");
            dataToSend.put("ski", user.skill);

            System.out.println(user.skill);

            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader = null;

            try {
                URL url = new URL(SERVER_ADDRESS + "deleteSkill.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(encodedStr);
                writer.flush();
                writer.close();
                os.close();

                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                line = sb.toString();

                Log.i("custom_check", "The values we have recieved are:");
                Log.i("custom_check", line);

                con.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }

    public class DeleteUserEducationAsyncTask extends AsyncTask<Void, Void, Void>{

        User user;
        GetUserCallback userCallback;

        public DeleteUserEducationAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("uid",user.uid+"");
            dataToSend.put("school", user.school);
            dataToSend.put("majory", user.major);
            dataToSend.put("graduation", user.grad);


            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader = null;

            try {
                URL url = new URL(SERVER_ADDRESS + "deleteEducation.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(encodedStr);
                writer.flush();
                writer.close();
                os.close();

                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                line = sb.toString();

                Log.i("custom_check", "The values we have recieved are:");
                Log.i("custom_check", line);

                con.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }

    public void deleteUserWorkInBackground(User user, GetUserCallback userCallback) {
        progressDialog.show();
        new DeleteUserWorkAsyncTask(user, userCallback).execute();
    }

    public class DeleteUserWorkAsyncTask extends AsyncTask<Void, Void, Void>{

        User user;
        GetUserCallback userCallback;

        public DeleteUserWorkAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("uid",user.uid+"");
            dataToSend.put("company", user.school);
            dataToSend.put("title", user.major);
            dataToSend.put("desc", user.grad);

            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader = null;

            try {
                URL url = new URL(SERVER_ADDRESS + "deleteWork.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(encodedStr);
                writer.flush();
                writer.close();
                os.close();

                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                line = sb.toString();

                Log.i("custom_check", "The values we have recieved are:");
                Log.i("custom_check", line);

                con.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }


    public class StoreIdeaAsyncTask extends AsyncTask<Void, Void, Void>{

        Idea idea;
        GetIdeaCallback ideaCallback;

        public StoreIdeaAsyncTask(Idea idea, GetIdeaCallback ideaCallback) {
            this.idea = idea;
            this.ideaCallback = ideaCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("uid",idea.uid+"");
            dataToSend.put("name", idea.name);
            dataToSend.put("desc", idea.desc);


            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader = null;

            try {
                URL url = new URL(SERVER_ADDRESS + "incubate.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(encodedStr);
                writer.flush();
                writer.close();
                os.close();

                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                line = sb.toString();

                Log.i("custom_check", "The values received in the store part are as follows:");
                Log.i("custom_check", line);

                con.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }




        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            ideaCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }

    public class StoreUserSkillAsyncTask extends AsyncTask<Void, Void, Void>{

        User user;
        GetUserCallback userCallback;

        public StoreUserSkillAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("uid",user.uid+"");
            dataToSend.put("skill", user.skill);

            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader = null;

            try {
                URL url = new URL(SERVER_ADDRESS + "skill.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(encodedStr);
                writer.flush();
                writer.close();
                os.close();

                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                line = sb.toString();

                Log.i("custom_check", "The values received in the store part are as follows:");
                Log.i("custom_check", line);

                con.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }

    //STORES THE WORK STUFF
    public class StoreUserWorkAsyncTask extends AsyncTask<Void, Void, Void>{

        User user;
        GetUserCallback userCallback;

        public StoreUserWorkAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("uid",user.uid+"");
            dataToSend.put("company", user.school);
            dataToSend.put("title", user.major);
            dataToSend.put("description", user.grad);

            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader = null;

            try {
                URL url = new URL(SERVER_ADDRESS + "work.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(encodedStr);
                writer.flush();
                writer.close();
                os.close();

                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                line = sb.toString();

                Log.i("custom_check", "The values received in the store part are as follows:");
                Log.i("custom_check", line);

                con.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }

    //STORES THE EDUCATION STUFF
    public class StoreUserEducationAsyncTask extends AsyncTask<Void, Void, Void>{

        User user;
        GetUserCallback userCallback;

        public StoreUserEducationAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("uid",user.uid+"");
            dataToSend.put("school", user.school);
            dataToSend.put("major", user.major);
            dataToSend.put("grad", user.grad);

            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader = null;

            try {
                URL url = new URL(SERVER_ADDRESS + "education.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(encodedStr);
                writer.flush();
                writer.close();
                os.close();

                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                line = sb.toString();

                Log.i("custom_check", "The values received in the store part are as follows:");
                Log.i("custom_check", line);

                con.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }


    //GETS THE IDEAS OF THE USER
    public class fetchUserIdeaAsyncTask extends AsyncTask<Void, Void, String>{

        User user;
        GetUserCallback userCallback;

        public fetchUserIdeaAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder sb = new StringBuilder();
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("uid", user.uid + "");

            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader;
            try {
                URL url = new URL(SERVER_ADDRESS + "fetchUserIdeas.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(encodedStr);
                writer.flush();
                writer.close();
                os.close();

                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                line = sb.toString();

                Log.i("custom_check", "The values received in the store part are as follows:");
                Log.i("custom_check", line);

                con.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            userCallback.donezo(s);
            super.onPostExecute(s);
        }
    }

    //GETS THE IDEAS OF THE USER
    public class fetchOtherUserIdeaAsyncTask extends AsyncTask<Void, Void, String>{

        User user;
        GetUserCallback userCallback;

        public fetchOtherUserIdeaAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder sb = new StringBuilder();
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("uid", user.uid + "");

            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader;
            try {
                URL url = new URL(SERVER_ADDRESS + "fetchOtherUserIdeas.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(encodedStr);
                writer.flush();
                writer.close();
                os.close();

                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                line = sb.toString();

                Log.i("custom_check", "The values received in the store part are as follows:");
                Log.i("custom_check", line);

                con.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            userCallback.donezo(s);
            super.onPostExecute(s);
        }
    }

    //GETS THE ATTRIBUTES OF THE USER
    public class fetchUserAttributesAsyncTask extends AsyncTask<Void, Void, String>{

        User user;
        GetUserCallback userCallback;

        public fetchUserAttributesAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder sb = new StringBuilder();
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("uid", user.uid + "");

            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader;
            try {
                URL url = new URL(SERVER_ADDRESS + "fetchUserAttributes.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(encodedStr);
                writer.flush();
                writer.close();
                os.close();

                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                line = sb.toString();

                Log.i("custom_check", "The values received in the store part are as follows:");
                Log.i("custom_check", line);



                con.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            userCallback.donezo(s);
            super.onPostExecute(s);
        }
    }

    //STORES DATA FOR REGISTERING PURPOSES
    public class StoreUserDataAsyncTask extends AsyncTask<Void, Void, Void> {

        User user;
        GetUserCallback userCallback;

        public StoreUserDataAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("fName", user.fName);
            dataToSend.put("lName", user.lName);
            dataToSend.put("username", user.username);
            dataToSend.put("password", user.password);
            dataToSend.put("email", user.email);
            dataToSend.put("dob", user.dob);

            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader = null;

            try {
                URL url = new URL(SERVER_ADDRESS + "register.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(encodedStr);
                writer.flush();
                writer.close();
                os.close();

                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                line = sb.toString();

                Log.i("custom_check", "The values received in the store part are as follows:");
                Log.i("custom_check", line);

                con.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }

    //GETS DATA FOR LOGIN PURPOSES
    public class fetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {

        User user;
        GetUserCallback userCallback;

        public fetchUserDataAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... params) {
            Map<String, String> dataToSend = new HashMap<>();
            dataToSend.put("username", user.username);
            dataToSend.put("password", user.password);

            String encodedStr = getEncodedData(dataToSend);
            BufferedReader reader = null;

            User returnedUser = null;

            try {
                URL url = new URL(SERVER_ADDRESS + "fetchUserData.php");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(15000);
                con.setConnectTimeout(15000);
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                OutputStream os = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(encodedStr);
                writer.flush();
                writer.close();
                os.close();

                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                line = sb.toString();

                Log.i("custom_check", "The values received in the store part are as follows:");
                Log.i("custom_check", line);

                JSONObject jObject = new JSONObject(line);

                if(jObject.length()== 0){
                    returnedUser = null;
                } else {
                    int uid = jObject.getInt("uid");
                    String fName = jObject.getString("fName");
                    String lName = jObject.getString("lName");
                    String email = jObject.getString("email");
                    String dob = jObject.getString("dob");

                    returnedUser = new User(uid, fName, lName, user.username, user.password, email, dob);
                }

                con.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return returnedUser;
        }

        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }
}
