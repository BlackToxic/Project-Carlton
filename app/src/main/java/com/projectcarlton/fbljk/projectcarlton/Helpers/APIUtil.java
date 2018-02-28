package com.projectcarlton.fbljk.projectcarlton.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.telecom.Call;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.APICallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.APIUtilCallback.APIUtilCallback;
import com.projectcarlton.fbljk.projectcarlton.API.Callback.CallbackType;
import com.projectcarlton.fbljk.projectcarlton.API.Exception.APIException;
import com.projectcarlton.fbljk.projectcarlton.API.Exception.APIExceptionType;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APIGetImageRequest;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APIGetRequest;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APILoginGetRequest;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APIPostImageRequest;
import com.projectcarlton.fbljk.projectcarlton.API.Request.APIPostRequest;
import com.projectcarlton.fbljk.projectcarlton.API.Request.VolleyMultipartRequest;
import com.projectcarlton.fbljk.projectcarlton.Activities.Core.GroupActivity;
import com.projectcarlton.fbljk.projectcarlton.Activities.Core.GroupsActivity;
import com.projectcarlton.fbljk.projectcarlton.Cache.SettingsCache;
import com.projectcarlton.fbljk.projectcarlton.Data.Grade;
import com.projectcarlton.fbljk.projectcarlton.Data.Group;
import com.projectcarlton.fbljk.projectcarlton.Data.Invite;
import com.projectcarlton.fbljk.projectcarlton.Data.User;
import com.projectcarlton.fbljk.projectcarlton.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class APIUtil implements APICallback {

    private String BASE_URL;

    private SimpleDateFormat dateFormat;

    private Context context;
    private APIUtilCallback handler;

    public APIUtil(Context context, APIUtilCallback handler) {
        this.dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        this.context = context;
        this.handler = handler;

        BASE_URL = context.getString(R.string.API_URL);
    }

    public APIUtilCallback getHandler() { return handler; }
    public void setHandler(APIUtilCallback context){ this.handler = handler; }

    public void getLoginAsync(String username, String password) {
        APILoginGetRequest request = new APILoginGetRequest(context.getApplicationContext(),this, CallbackType.LOGIN_CALLBACK);
        request.execute(BASE_URL, username, password);
    }

    public void getLogoutAsync(String userid) {
        String apiUrl = BASE_URL + "user?logout=1&userid=" + userid;
        APIGetRequest request = new APIGetRequest(context.getApplicationContext(), this, CallbackType.LOGOUT_CALLBACK);
        request.execute(apiUrl);
    }

    public void getRegisterAsync(String email, String username, String password) {
        String apiUrl = BASE_URL + "user?register=1&email=" + email + "&username=" + username + "&password=" + password;
        APIGetRequest request = new APIGetRequest(context.getApplicationContext(),this, CallbackType.REGISTER_CALLBACK);
        request.execute(apiUrl);
    }

    public void loadGroupsAsync(String userid) {
        String apiUrl = BASE_URL + "group?userid=" + userid;
        APIGetRequest request = new APIGetRequest(context.getApplicationContext(),this, CallbackType.LOADINGGROUPS_CALLBACK);
        request.execute(apiUrl);
    }

    public void leaveGroupAsync(String groupid, String userid) {
        String apiUrl = BASE_URL + "group?leave=1&groupid=" +groupid + "&userid=" + userid;
        APIGetRequest request = new APIGetRequest(context.getApplicationContext(),this, CallbackType.LEAVEGROUP_CALLBACK);
        request.execute(apiUrl);
    }

    public void deleteGroupAsync(String groupid) {
        String apiUrl = BASE_URL + "group?delete=1&groupid=" + groupid;
        APIGetRequest request = new APIGetRequest(context.getApplicationContext(),this, CallbackType.DELETEGROUP_CALLBACK);
        request.execute(apiUrl);
    }

    public void loadInvitesAsync(String userid) {
        String apiUrl = BASE_URL + "invite?userid=" + userid;
        APIGetRequest request = new APIGetRequest(context.getApplicationContext(),this, CallbackType.LOADINGINVITES_CALLBACK);
        request.execute(apiUrl);
    }

    public void searchUsersAsync(String username, String groupid) {
        String apiUrl = BASE_URL + "user?username=" + username + "&groupid=" + groupid;
        APIGetRequest request = new APIGetRequest(context.getApplicationContext(),this, CallbackType.SEARCHUSERS_CALLBACK);
        request.execute(apiUrl);
    }

    public void createGroupAsync(String groupname, String groupdesc, String adminid) {
        String apiUrl = BASE_URL + "group?userid=" + adminid + "&groupname=" + groupname + "&groupdescription=" + groupdesc;
        APIGetRequest request = new APIGetRequest(context.getApplicationContext(),this, CallbackType.CREATEGROUP_CALLBACK);
        request.execute(apiUrl);
    }

    public void loadUsersByGroupAsync(String groupid) {
        String apiUrl = BASE_URL + "user?groupid=" + groupid;
        APIGetRequest request = new APIGetRequest(context.getApplicationContext(),this, CallbackType.LOADUSERS_CALLBACK);
        request.execute(apiUrl);
    }

    public void inviteUserAsync(String groupid, String senderid, String receiverid) {
        String apiUrl = BASE_URL + "invite?groupid=" + groupid + "&fromuserid=" + senderid + "&userid=" + receiverid;
        APIGetRequest request = new APIGetRequest(context.getApplicationContext(),this, CallbackType.INVITEUSER_CALLBACK);
        request.execute(apiUrl);
    }

    public void acceptInviteAsync(String inviteid, String userid) {
        String apiUrl = BASE_URL + "invite?inviteid=" + inviteid + "&userid=" + userid + "&accept=1";
        APIGetRequest request = new APIGetRequest(context.getApplicationContext(),this, CallbackType.ACCEPTINVITE_CALLBACK);
        request.execute(apiUrl);
    }

    public void rejectInviteAsync(String inviteid, String userid) {
        String apiUrl = BASE_URL + "invite?inviteid=" + inviteid + "&userid=" + userid + "&accept=0";
        APIGetRequest request = new APIGetRequest(context.getApplicationContext(),this, CallbackType.REJECTINVITE_CALLBACK);
        request.execute(apiUrl);
    }

    public void getImageAsync(String imagename) {
        APIGetImageRequest request = new APIGetImageRequest(context.getApplicationContext(), this, CallbackType.DOWNLOADIMAGE_CALLBACK);
        request.execute(BASE_URL + "image?imagename=" + imagename);
    }

    public void updateUserAsync(String userid, String username, String useremail) {
        APIPostRequest request = new APIPostRequest(context.getApplicationContext(), this, CallbackType.UPLOADIMAGE_CALLBACK);
        request.addParameter("userid", userid);
        request.addParameter("username", username);
        request.addParameter("useremail", useremail);
        request.execute(BASE_URL + "user");
    }

    public void uploadProfileImage(Bitmap image, String userid, String username, String useremail) {
        APIPostImageRequest request = new APIPostImageRequest(context.getApplicationContext(), this, CallbackType.UPLOADIMAGE_CALLBACK);
        request.addParameter("userid", userid);
        request.addParameter("username", username);
        request.addParameter("useremail", useremail);
        request.execute(BASE_URL + "user", image);
    }

    public void getGradesForUserAsync(String userid) {
        String apiUrl = BASE_URL + "grade?userid=" + userid + "&groupid=" + SettingsCache.CURRENTGROUP.groupId;
        APIGetRequest request = new APIGetRequest(context.getApplicationContext(), this, CallbackType.GETGRADESFORUSER_CALLBACK);
        request.execute(apiUrl);
    }

    public void getGradesByGroupAsync() {
        String apiUrl = BASE_URL + "grade?groupid=" + SettingsCache.CURRENTGROUP.groupId;
        APIGetRequest request = new APIGetRequest(context.getApplicationContext(), this, CallbackType.GETGRADESBYGROUP_CALLBACK);
        request.execute(apiUrl);
    }

    public void createGradeAsync(String userid, int note, Date datum) {
        APIPostRequest request = new APIPostRequest(context.getApplicationContext(), this, CallbackType.CREATEGRADE_CALLBACK);
        request.addParameter("groupid", SettingsCache.CURRENTGROUP.groupId);
        request.addParameter("userid", userid);
        request.addParameter("note", Integer.toString(note));
        request.addParameter("datum", dateFormat.format(datum));
        request.execute(BASE_URL + "grade");
    }

    @Override
    public void callback(int callbackType, Object result) {
        try {
            if (callbackType == CallbackType.LOGIN_CALLBACK) {
                if (result != null && result instanceof String && !result.equals("")) {
                    JSONObject resultObject = new JSONObject((String) result);

                    if (resultObject.has("code")) {
                        APIException exception = new APIException(resultObject.getInt("code"), "");
                        handler.callback(callbackType, exception);
                    } else if (resultObject.has("username")) {
                        User user = new User();
                        user.userId = resultObject.getString("id");
                        user.userName = resultObject.getString("username");
                        user.userPassword = resultObject.getString("hash");
                        user.userEmail = resultObject.getString("useremail");
                        user.userPhoto = resultObject.getString("userphoto");

                        handler.callback(callbackType, user);
                    } else {
                        handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                    }
                } else {
                    handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                }
            } else if (callbackType == CallbackType.LOGOUT_CALLBACK) {
                if (result != null && result instanceof String && !result.equals("")) {
                    JSONObject resultObject = new JSONObject((String)result);

                    if (resultObject.has("code")) {
                        APIException exception = new APIException(resultObject.getInt("code"), "");
                        handler.callback(callbackType, exception);
                    } else if (resultObject.has("id")) {
                        handler.callback(callbackType, true);
                    } else {
                        handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                    }
                } else {
                    handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                }
            } else if (callbackType == CallbackType.REGISTER_CALLBACK) {
                if (result != null && result instanceof String && !result.equals("")) {
                    JSONObject resultObject = new JSONObject((String)result);

                    if (resultObject.has("code")) {
                        APIException exception = new APIException(resultObject.getInt("code"), "");
                        handler.callback(callbackType, exception);
                    } else if (resultObject.has("id")) {
                        handler.callback(callbackType, true);
                    } else {
                        handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                    }
                } else {
                    handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                }
            } else if (callbackType == CallbackType.LOADINGGROUPS_CALLBACK) {
                if (result != null && result instanceof String && !result.equals("")) {
                    if (((String)result).contains("groupid")) {
                        ArrayList<Group> groups = new ArrayList<Group>();

                        JSONArray array = new JSONArray((String)result);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject childObject = array.getJSONObject(i);

                            Group newGroup = new Group();
                            newGroup.groupId = childObject.getString("groupid");
                            newGroup.groupName = childObject.getString("groupname");
                            newGroup.groupDescription = childObject.getString("groupdescription");
                            newGroup.groupAdmin = childObject.getString("groupadmin");
                            newGroup.groupPhoto = childObject.getString("groupphoto");
                            groups.add(newGroup);
                        }

                        handler.callback(callbackType, groups);
                    } else {
                        JSONObject resultObject = new JSONObject((String)result);

                        if (resultObject.has("code")) {
                            APIException exception = new APIException(resultObject.getInt("code"), "");
                            handler.callback(callbackType, exception);
                        } else {
                            handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                        }
                    }
                } else {
                    handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                }
            } else if (callbackType == CallbackType.LEAVEGROUP_CALLBACK || callbackType == CallbackType.DELETEGROUP_CALLBACK) {
                if (result != null && result instanceof String && !result.equals("")) {
                    if (((String)result).contains("code")) {
                        JSONObject resultObject = new JSONObject((String)result);

                        if (resultObject.has("code")) {
                            APIException exception = new APIException(resultObject.getInt("code"), "");
                            handler.callback(callbackType, exception);
                        } else {
                            handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                        }
                    } else if (((String)result).equals("1")) {
                        handler.callback(callbackType, true);
                    } else {
                        handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                    }
                } else {
                    handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                }
            } else if (callbackType == CallbackType.LOADINGINVITES_CALLBACK) {
                if (result != null && result instanceof String && !result.equals("")) {
                    if (((String)result).contains("id")) {
                        ArrayList<Invite> invites = new ArrayList<Invite>();

                        JSONArray array = new JSONArray((String)result);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject childObject = array.getJSONObject(i);

                            Invite invite = new Invite();
                            invite.inviteId = childObject.getString("id");
                            invite.senderName = "von " + childObject.getString("sender");
                            invite.groupName = childObject.getString("groupname");
                            invites.add(invite);
                        }

                        handler.callback(callbackType, invites);
                    } else {
                        JSONObject resultObject = new JSONObject((String)result);

                        if (resultObject.has("code")) {
                            APIException exception = new APIException(resultObject.getInt("code"), "");
                            handler.callback(callbackType, exception);
                        } else {
                            handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                        }
                    }
                } else {
                    handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                }
            } else if (callbackType == CallbackType.SEARCHUSERS_CALLBACK) {
                if (result != null && result instanceof String && !result.equals("")) {
                    if (((String)result).contains("id")) {
                        ArrayList<User> users = new ArrayList<User>();

                        JSONArray array = new JSONArray((String)result);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject childObject = array.getJSONObject(i);

                            User user = new User();
                            user.userId = childObject.getString("id");
                            user.userName = childObject.getString("username");
                            users.add(user);
                        }

                        handler.callback(callbackType, users);
                    } else {
                        JSONObject resultObject = new JSONObject((String)result);

                        if (resultObject.has("code")) {
                            APIException exception = new APIException(resultObject.getInt("code"), "");
                            handler.callback(callbackType, exception);
                        } else {
                            handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                        }
                    }
                } else {
                    handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                }
            } else if (callbackType == CallbackType.CREATEGROUP_CALLBACK) {
                if (result != null && result instanceof String && !result.equals("")) {
                    JSONObject resultObject = new JSONObject((String)result);

                    if (resultObject.has("code")) {
                        APIException exception = new APIException(resultObject.getInt("code"), "");
                        handler.callback(callbackType, exception);
                    } else if (resultObject.has("id")) {
                        handler.callback(callbackType, true);
                    } else {
                        handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                    }
                } else {
                    handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                }
            } else if (callbackType == CallbackType.LOADUSERS_CALLBACK) {
                if (result != null && result instanceof String && !result.equals("")) {
                    if (((String)result).contains("id")) {
                        ArrayList<User> users = new ArrayList<User>();

                        JSONArray array = new JSONArray((String)result);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject childObject = array.getJSONObject(i);

                            User user = new User();
                            user.userId = childObject.getString("id");
                            user.userName = childObject.getString("username");

                            if (!user.userId.equals(SettingsCache.CURRENTUSER.userId))
                                users.add(user);
                        }

                        handler.callback(callbackType, users);
                    } else {
                        JSONObject resultObject = new JSONObject((String)result);

                        if (resultObject.has("code")) {
                            APIException exception = new APIException(resultObject.getInt("code"), "");
                            handler.callback(callbackType, exception);
                        } else {
                            handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                        }
                    }
                } else {
                    handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                }
            } else if (callbackType == CallbackType.INVITEUSER_CALLBACK) {
                if (result != null && result instanceof String && !result.equals("")) {
                    if (((String)result).contains("code")) {
                        JSONObject resultObject = new JSONObject((String)result);

                        if (resultObject.has("code")) {
                            APIException exception = new APIException(resultObject.getInt("code"), "");
                            handler.callback(callbackType, exception);
                        } else {
                            handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                        }
                    } else if (((String)result).equals("1")) {
                        handler.callback(callbackType, true);
                    } else {
                        handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                    }
                } else {
                    handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                }
            } else if (callbackType == CallbackType.ACCEPTINVITE_CALLBACK || callbackType == CallbackType.REJECTINVITE_CALLBACK) {
                if (result != null && result instanceof String && !result.equals("")) {
                    if (((String)result).contains("code")) {
                        JSONObject resultObject = new JSONObject((String)result);

                        if (resultObject.has("code")) {
                            APIException exception = new APIException(resultObject.getInt("code"), "");
                            handler.callback(callbackType, exception);
                        } else {
                            handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                        }
                    } else if (((String)result).equals("1")) {
                        handler.callback(callbackType, true);
                    } else {
                        handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                    }
                } else {
                    handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                }
            } else if (callbackType == CallbackType.DOWNLOADIMAGE_CALLBACK) {
                if (result != null && result instanceof Bitmap) {
                    handler.callback(callbackType, result);
                } else {
                    handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                }
            } else if (callbackType == CallbackType.UPLOADIMAGE_CALLBACK) {
                if (result != null && result instanceof String) {
                    handler.callback(callbackType, result);
                } else {
                    handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                }
            } else if (callbackType == CallbackType.GETGRADESFORUSER_CALLBACK) {
                if (result != null && result instanceof String && !result.equals("")) {
                    JSONObject resultObject = new JSONObject((String) result);

                    if (resultObject.has("code")) {
                        APIException exception = new APIException(resultObject.getInt("code"), "");
                        handler.callback(callbackType, exception);
                    } else if (resultObject.has("gradeid")) {
                        ArrayList<Grade> grades = new ArrayList<Grade>();

                        JSONArray array = new JSONArray((String)result);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject childObject = array.getJSONObject(i);

                            Grade grade = new Grade();
                            grade.gradeId = childObject.getString("gradeid");
                            grade.groupId = childObject.getString("groupid");
                            grade.userId = childObject.getString("userid");
                            grade.note = childObject.getInt("note");
                            try {
                                grade.datum = dateFormat.parse(childObject.getString("datum"));
                            } catch (Exception e) {
                            }

                            grades.add(grade);
                        }

                        handler.callback(callbackType, grades);
                    } else {
                        handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                    }
                } else {
                    handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                }
            } else if (callbackType == CallbackType.GETGRADESBYGROUP_CALLBACK) {
                if (result != null && result instanceof String && !result.equals("")) {
                    JSONObject resultObject = new JSONObject((String) result);

                    if (resultObject.has("code")) {
                        APIException exception = new APIException(resultObject.getInt("code"), "");
                        handler.callback(callbackType, exception);
                    } else if (resultObject.has("gradeid")) {
                        ArrayList<Grade> grades = new ArrayList<Grade>();

                        JSONArray array = new JSONArray((String)result);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject childObject = array.getJSONObject(i);

                            Grade grade = new Grade();
                            grade.gradeId = childObject.getString("gradeid");
                            grade.groupId = childObject.getString("groupid");
                            grade.userId = childObject.getString("userid");
                            grade.note = childObject.getInt("note");
                            try {
                                grade.datum = dateFormat.parse(childObject.getString("datum"));
                            } catch (Exception e) {
                            }

                            grades.add(grade);
                        }

                        handler.callback(callbackType, grades);
                    } else {
                        handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                    }
                } else {
                    handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                }
            } else if (callbackType == CallbackType.CREATEGRADE_CALLBACK) {
                if (result != null && result instanceof String && !result.equals("")) {
                    JSONObject resultObject = new JSONObject((String)result);

                    if (resultObject.has("code")) {
                        APIException exception = new APIException(resultObject.getInt("code"), "");
                        handler.callback(callbackType, exception);
                    } else if (resultObject.has("id")) {
                        handler.callback(callbackType, true);
                    } else {
                        handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                    }
                } else {
                    handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
                }
            }
        } catch (JSONException e){
            handler.callback(callbackType, new APIException(APIExceptionType.UNKNOWN_ERROR, ""));
        }
    }
}