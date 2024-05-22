$(document).ready(function () {
  $("video").hover(
    function () {
      $(this).prop("controls", true); // Add controls on hover
    },
    function () {
      $(this).removeProp("controls"); // Remove controls when not hovering
    }
  );
});
