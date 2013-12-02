// dlzka prechodu obrazka
var animacia = 300;
// dlzka, po ktoru sa obrazok zobrazi
var delay = 4000;
// handler casovaca
var casovac = false;
// blokovacia premenna pri animacii
var free = true;


$(window).load(function(){
});

$(document).ready(function(){
	
	$('.show_more').click(function(event){
		$(this).parent().hide();
		$(this).parent().parent().find('.show_more_display').removeClass('invisible');
		event.preventDefault();
	});

    // spustenie animacie slideshow	na indexe
    casovac = setInterval("nextImage()", delay);

    // kliknutie pri vybere akcie
    $('.controller_unit').click(function(event) {
        if(free) {
            free = false;
            var ide = $(this).attr('id').substr(1);
            if(ide != $('.animation_controller_active').attr('id').substr(1)) {
                clearInterval(casovac);
                $('#animation_controller').find('div').removeClass('animation_controller_active');
                $('#c'+ide).addClass('animation_controller_active');
                $('.animate_active').fadeOut(animacia, function(){ $(this).removeClass('animate_active'); });
                $('#n'+ide).fadeIn(animacia, function(){
                    $('.animate').removeClass('animate_active');
                    $('#n'+ide).addClass('animate_active');
                    casovac = setInterval("nextImage()", delay);
                    free = true;
                });
            } else {
                free = true;
            }
        }
        event.preventDefault();
    });

    $('.lbox').lightBox({
        imageLoading: '/resources/img/lightbox/lightbox-ico-loading.gif',
        imageBtnClose: '/resources/img/lightbox/lightbox-btn-close.gif',
        imageBtnPrev: '/resources/img/lightbox/lightbox-btn-prev.gif',
        imageBtnNext: '/resources/img/lightbox/lightbox-btn-next.gif',
        imageBlank: '/resources/img/lightbox/lightbox-blank.gif',
        containerResizeSpeed: 200,
        txtImage: 'Obrázok',
        txtOf: '/'
    });
});

function nextImage(){
    if(free) {
        free = false;
        if($('.animate_active').next('.animate').size()==0) $('.animate:first').addClass('animate_active_next');
        else $('.animate_active').next('.animate').addClass('animate_active_next');
        if($('.animate_active_next').length) var ide = $('.animate_active_next').attr('id').substr(1);
        $('#animation_controller div').removeClass('animation_controller_active');
        $('#c'+ide).addClass('animation_controller_active');
        $('.animate_active').fadeOut(animacia, function(){ $(this).removeClass('animate_active'); });
        $('.animate_active_next').fadeIn(animacia, function(){
            $(this).removeClass('animate_active_next');
            $('.animate').removeClass('animate_active');
            $(this).addClass('animate_active');
            free = true;
        });
    }
}