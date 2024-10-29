const viewContactModal=document.getElementById("view_contact_modal")
const baseUrl='http://localhost:8081';
// const baseUrl='https://scm-2-0-v1.onrender.com';

const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
    id: 'view_contact_modal',
    override: true
};

const contactModal=new Modal(viewContactModal,options);

function  openContactModal(){
    contactModal.show();
}

function  closeContactModal(){
    contactModal.hide();
}


 async function loadContactData(id){

  try{
      const  data =await (await fetch(`${baseUrl}/api/contacts/${id}`))
          .json();
       debugger
      console.log(data)
      document.querySelector('#contact-name').innerHTML=data.name;
      document.querySelector('#contact-email').innerHTML=data.email;
      document.querySelector('#contact-name2').innerHTML=data.name;
      document.querySelector('#email').innerHTML=data.email;
      document.querySelector('#address').innerHTML=data.address;
      document.querySelector('#phone').innerHTML=data.phoneNumber;
      document.querySelector('#about').innerHTML=data.description;
      document.querySelector('#contact-img').src=data.picture;
      document.querySelector('#website1').src=data.websiteLink;
      document.querySelector('#website2').src=data.linkedInLink;
      document.querySelector('#website1').innerHTML=data.websiteLink;
      document.querySelector('#website2').innerHTML=data.linkedInLink;
      document.querySelector('#update-link').href = `/user/contacts/update-view/${data.id}`;
      const heartIcon = document.querySelector('#heart-icon');
      if (data.favorite){

          heartIcon.classList.add('text-red-500');
          heartIcon.classList.add('hover:text-red-700');
      }
      else {

          heartIcon.classList.remove('text-red-500');
          heartIcon.classList.remove('hover:text-red-700');
      }


      openContactModal();
  }
  catch (error){
      console.log(error);
  }

}


async  function deleteContact(id){
    Swal.fire({
        title: "Are you sure?",
        text: "You won't be able to revert contact!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!"
    }).then((result) => {
        if (result.isConfirmed) {
               let  url=`${baseUrl}/user/contacts/delete/${id}`;
               window.location.replace(url)
            Swal.fire({
                title: "Deleted!",
                text: "Your Contact has been deleted.",
                icon: "success"
            });
        }
    });
}


