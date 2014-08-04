package net.haikuDev.loginrest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	Button btnLogin;
	Button btnCloseSession;
	EditText usr;
	EditText pwd;
	TextView txtWelcome;
	TextView txtNombUser;
	Integer userId;
	LoginService loginService;
	LogoutService logoutService;
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        btnCloseSession=(Button)findViewById(R.id.btnCloseSession);
        usr=(EditText)findViewById(R.id.txtUsr);
        pwd=(EditText)findViewById(R.id.txtPwd);
        txtWelcome=(TextView)findViewById(R.id.txtWelcome);
        txtNombUser=(TextView)findViewById(R.id.txtUser);
        btnLogin.setOnClickListener(this);
        btnCloseSession.setOnClickListener(this);
        
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			String txtUsr=usr.getText().toString();
			String txtPwd=pwd.getText().toString();
			if(!txtUsr.equals("")&&!txtPwd.equals("")){
				loginService= new LoginService();
				loginService.execute(txtUsr,txtPwd);
			}else{
				Toast.makeText(this, "Debe introducir usuario y contraseña", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.btnCloseSession:
			logoutService= new LogoutService();
			logoutService.execute(userId);
			break;

		default:
			break;
		}
	}

	@SuppressLint("NewApi") 
	private class LoginService extends AsyncTask<String, String, Boolean>{
		JSONObject responseJSON;
		@Override
		protected Boolean doInBackground(String... params) {
			boolean result=true;
			HttpClient httpClient= new DefaultHttpClient();
			HttpPost post= new HttpPost("URL");
			post.setHeader("content-type","application/json");
			
			//Construir objeto
			JSONObject dato= new JSONObject();
			try {
				dato.put("user", params[0]);
				dato.put("pass", params[1]);
				dato.put("token","");
				StringEntity entity=new StringEntity(dato.toString());
				post.setEntity(entity);
				HttpResponse resp=httpClient.execute(post);
				String respStr= EntityUtils.toString(resp.getEntity());
				JSONObject respJSON=new JSONObject(respStr);
				if(!respJSON.getBoolean("Exito")){
					result=false;
				}
				else{
					responseJSON=respJSON;
				}
				
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;
		}
		protected void onPostExecute(Boolean result) {
			 if(result){
				 try {
					JSONObject datos=responseJSON.getJSONObject("Datos");
					txtWelcome.setAlpha(1.0f);
					txtNombUser.setText(datos.getString("Nombre"));
					userId=datos.getInt("UserId");
					btnCloseSession.setAlpha(1.0f);
					usr.setAlpha(0.0f);
					pwd.setAlpha(0.0f);
					btnLogin.setAlpha(0.0f);
					usr.setText("");
					pwd.setText("");
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		 }
	}
	 
	@SuppressLint("NewApi") 
	private class LogoutService extends AsyncTask<Integer, String, Boolean>{
		JSONObject responseJSON;
		
		@Override
		protected Boolean doInBackground(Integer... params) {
			boolean result=true;
			HttpClient httpClient= new DefaultHttpClient();
			HttpPost post= new HttpPost("URL");
			post.setHeader("content-type","application/json");
			JSONObject dato = new JSONObject();
			try {
				dato.put("userId",params[0]);
				dato.put("token", "");
				StringEntity entity=new StringEntity(dato.toString());
				post.setEntity(entity);
				HttpResponse resp=httpClient.execute(post);
				String respStr=EntityUtils.toString(resp.getEntity());
		            System.out.println("datos :"+respStr);
		            JSONObject respJSON = new JSONObject(respStr);
		 
		            if(!respJSON.getBoolean("Exito")){
		            	 result = false;
		            }
		            else{
		            	responseJSON=respJSON;
		            }		               
		        
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			return result;
		}
		protected void onPostExecute(Boolean result) {
		        if (result)
		        {
		        	txtWelcome.setAlpha(0.0f);
				txtNombUser.setText("");
				btnCloseSession.setAlpha(0.0f);
				usr.setAlpha(1.0f);
				pwd.setAlpha(1.0f);
				btnLogin.setAlpha(1.0f);
		        }
		    }	
		
	}
}

