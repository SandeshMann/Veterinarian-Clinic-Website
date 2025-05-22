let medRecordId = null
function setMedRecordId(id){
    medRecordId = id;
    console.log(medRecordId);
}

document.addEventListener('DOMContentLoaded', function() {
    // Get all the view details buttons
    const buttons = document.querySelectorAll('.viewDetailsButton');
    buttons.forEach(button => {
      button.addEventListener('click', function() {
        const petId = this.getAttribute('data-pet-id');
        // Fetch pet details from server or local storage
        fetch(`/medicalrecords/${medRecordId}`)
          .then(response => response.json())
          .then(data => {
            // Assuming data contains pet details
            const modalBody = document.getElementById('modalBodyContent');

            modalBody.innerHTML = `
              <div id="content-to-pdf">
                <p><strong>Pet:</strong> ${data.pet.petName}</p>
                <p><strong>Record Type:</strong> </br>${data.recordType}</p>
                <p><strong>Patient:</strong> ${data.patient.firstName} ${data.patient.lastName}</p>
                <p><strong>Clinic:</strong> ${data.clinicName}</p>
                <p><strong>Date Issued:</strong> ${new Date(data.dateIssued).toLocaleDateString()}</p>
                </br>
                <p><strong>Details:</strong> </br>${data.description}</p>
                </br>
                
              </div>
            `;

          })
          .catch(error => {
            console.error('Error fetching pet details:', error);
          });
      });
    });
  });

  // Download

  document.getElementById('download-btn').addEventListener('click', function () {
    // Access jsPDF from the window
    const { jsPDF } = window.jspdf;

    // Create a new jsPDF instance
    const doc = new jsPDF({
        orientation: 'portrait',
        unit: 'mm',
        format: 'a4',
        putOnlyUsedFonts: true,
        floatPrecision: 5 // or a lower value if needed
    });

    // Grab the HTML content to be converted
    const content = document.getElementById('content-to-pdf');

    // Set options for fitting content to a single page
    doc.html(content, {
        callback: function (doc) {
            // Save the PDF with a filename
            doc.save('medical-record.pdf');
        },
        x: 10,  
        y: 10,  
        html2canvas: {
            scale: 0.25 
        }
    });
});


// Email
// handle email sending
document.getElementById('sendEmail-btn').addEventListener('click', function() {
  const email = document.getElementById('emailMedInput').value;

  if (!email) {
    alert('Please enter a valid email.');
    return;
  }
  console.log(email);
  // Generate PDF as before
  const { jsPDF } = window.jspdf;
  const doc = new jsPDF({
      orientation: 'portrait',
      unit: 'mm',
      format: 'a4',
      putOnlyUsedFonts: true,
      floatPrecision: 5
  });

  const content = document.getElementById('content-to-pdf');

  doc.html(content, {
      callback: function (doc) {
          const pdfData = doc.output('blob');

          // Send the PDF to the server to handle email sending
          const formData = new FormData();
          formData.append('email', email);
          formData.append('pdf', pdfData, 'medical-record.pdf');

          fetch('/emails/send-email', {
              method: 'POST',
              body: formData
          })
          .then(response => response.json())
          .then(data => {
              if (data.success) {
                  alert('Email sent successfully!');
              } else {
                  alert('Failed to send email.');
              }
          })
          .catch(error => {
              console.error('Error sending email:', error);
              alert('Error sending email.');
          });
      },
      x: 10,
      y: 10,
      html2canvas: {
          scale: 0.25
      }
  });
});



