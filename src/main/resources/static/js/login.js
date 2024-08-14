/*var ajax_call_enabled = false;

function lhsIsNull(value) {
	try {                                                                                                                                                                                                                                                                                                                                                                                   
		value = value.trim();
	} catch (err) {
		value = value;
	}
	if (value !== "" && value !== "null" && value !== null
			&& value !== undefined && value !== 'undefined')
		return false;
	else
		return true;
}// end lhsIsNull function

*/






 function validateLogin() {
      var username = document.getElementById('username').value;
      var password = document.getElementById('password').value;
      var cp = document.getElementById("globalcontextpath").value;

      // Perform simple validation
      if (username === 'lhs' && password === 'lhs@123') {
        // Redirect to the PDF page (replace 'pdf_page.html' with your actual PDF page)
        window.location.href = cp+'api/PDFTextExtract';
    	 
      } else {
        // Display error message
        document.getElementById('error-message').innerText = 'Invalid username or password. Please try again.';
      }
    }
    
    
    
    function login(){
		var cp = document.getElementById("globalcontextpath").value;
		var login_id = document.getElementById('username').value;
		var login_pwd = document.getElementById('password').value;
				
		var url = cp+"api/loginAuth?login_id=" + login_id + "&login_pwd=" + login_pwd;
		 window.location.href = url;
	}
	
	
	

 function click_me(){
	 var cp = document.getElementById("globalcontextpath").value;
	 alert('clickme');
	var url= cp+"api/userForm";
	 window.location.href= url;
 }


/*function save(){	
	 var cp = document.getElementById("globalcontextpath").value;
	 var login_id = document.getElementById('loginId').value;
	 var login_pwd = document.getElementById('loginPwd').value;
		
	alert('login_id'--+login_id);
	alert('login_pwd--'+login_pwd);
	var url= cp+"api/userForm?login_id=" + login_id + "&login_pwd=" + login_pwd;
	 window.location.href= url;
 }*/
	 