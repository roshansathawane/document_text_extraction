
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




function fileUpload() {
	var fileInput = document.getElementById("file");
			
	if (fileInput.files.length > 0) {
		/*$('#Upload').modal('show');*/
		var formData = new FormData();
		formData.append("file", fileInput.files[0]);

		var myForm = document.createElement("form");
		myForm.method = "post";
		myForm.enctype = "multipart/form-data";
		myForm.className = "d-file";
		myForm.id = "myForm";

		var cp = document.getElementById("globalcontextpath").value;
		var url = cp + "api/msDocumentdoc";
		console.log('URL:', url);
		
		myForm.action = url;
		myForm.appendChild(fileInput.cloneNode()); // Append a clone of the file input to the form
	/*	document.getElementById('file').style.display='none';*/
		document.body.appendChild(myForm);

		/*document.getElementById('main_panel').style.display = "block";*/
		
		myForm.submit();
	} else {
		var errorMessage = document.getElementById("error-message");
		errorMessage.innerText = "Please Select Input File.";
		console.log('No file selected.');
	}
}



async function uploadFile1() {
	let fileInput = document.getElementById("file");
	
	if (fileInput.files.length > 0) {
		let formData = new FormData();
		formData.append("file", fileInput.files[0]);

		try {
			// $('#loadermodal').modal('show');

			let response = await fetch('./msDocument', {
				method: "POST",
				body: formData
			});

			if (response.status == 200) {
				showLoader1();
			//	fileUpload();
/*
				// Ensure the input tag's position remains constant
				fileInput.style.position = "absolute";
				fileInput.style.left = "-9999px";
				fileInput.style.top = "-9999px";*/

		/*		document.getElementById('uploadbt').style.display = "block";
				document.getElementById('uploadIdbtn').style.display = "none";
				document.getElementById('file').style.display = "none";*/
				
			} else {
				console.log("Error uploading file. Please try again.");
			}
		} catch (error) {
			console.error("Error uploading file:", error);
		}
	} else {
		var errorMessage = document.getElementById("error-message");
		errorMessage.innerText = "Please Select Input File.";
		console.log('No file selected.');
	}
}


 function showLoader() { 
        document.getElementById('loader').style.display="block";
        setTimeout(function () {
             document.getElementById('loader').style.display="none";
            text_convert();
        }, 3000);
    }


function showLoader1() { 
        document.getElementById('loader').style.display="block";
        setTimeout(function () {
			fileUpload();
             document.getElementById('loader').style.display="none";
            
        }, 4000);
    }




function text_convert(){	
	document.getElementById('textdata').style.display="none";
	document.getElementById('main_panal').style.display="block";
}


function downloadAll(){
	document.getElementById('downloadall').style.display="block";	
}

function download_zipfile(){
	var cp = document.getElementById("globalcontextpath").value;
	var url= cp+ "api/downloadAll";
	window.location.href=url;
}


function download_individual_page() {
    var cp = document.getElementById("globalcontextpath").value;
    var pageNo = document.getElementById("pageToDownload").value;
    var total_page = document.getElementById("totalpage").value;
  

    if (pageNo !== null && pageNo !== "" && !isNaN(pageNo) && pageNo > 0 && pageNo <= total_page){
		 
        var url = cp + "api/downloadPage?pageNo=" + pageNo;
        window.location.href = url;
    } else {
       /* alert("Invalid page number. Please enter a valid page number.");*/
      warning_model1();
      
      setDynamicMessage("Please enter a valid page number within the range of available pages.");
    }
}



function download_individual_allpages() {
    let fileInput = document.getElementById("file");
     let file_type = document.getElementById("inputFileType").value;

    if (
        file_type === "application/pdf" ||
        file_type === "application/vnd.openxmlformats-officedocument.wordprocessingml.document" ||
        file_type === "application/vnd.openxmlformats-officedocument.presentationml.presentation") {
      
        var cp = document.getElementById("globalcontextpath").value;
        var url = cp + "api/downloadAllSeperatePages";
        window.location.href = url;
    } else {
        warning_model2();
       
    }
}



function warning_model1() {
    $('#warning1').modal('show');
}

function warning_model2() {
    $('#warning2').modal('show');
}



