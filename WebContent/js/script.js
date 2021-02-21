/**
 *
 */

$(function() {
    //アラートモーダル表示
    $('#flush_modal').fadeIn();

    //日報登録フォーム表示
     $('#report-show').click(function() {
            $('#report-creat').fadeIn();
      });
    //モーダルclose
     $('.close-modal').click(function() {
        $('.active').removeClass('active');
        $('body *').removeAttr('style');
        $('#report-creat').fadeOut();
        $('.defaultModal').fadeOut();
        $('#flush_modal').fadeOut();
        $('#time-out-creat').fadeOut();
        $('.calendar_list').fadeOut();
        $('#time-out-creat').fadeOut();
     });

    //退勤時のモーダル
     $('#time-out').click(function() {
         $('#time-out-creat').fadeIn();
     });

     //有給編集時のモーダル
     $('#vacation').click(function() {
         $('#vacation-creat').fadeIn();
     });

      //フォームモーダル表示
      $('.newtest').click(function() {
          $('.active').removeClass('active');
          //インデックス番号取得
          var clickedOption = $(this).attr('data-option');
        //クリックしたインデックス番号と同じリスト表示
        $('.defaultModal').eq(clickedOption).addClass('active');
        $('.active').show();
      });

      //勤務データ　カレンダーモーダル
      $('.calendarDay').click(function() {
          $('.active').removeClass('active');
          var clickedOptionDay = $(this).attr('data-option');//日付
          var clickedOptionMonth = $(this).attr('data-month');//月
          $('.day').text(clickedOptionMonth+clickedOptionDay+'日の勤務記録');
         //空白の場合は表示しない
          if(clickedOptionDay!=""){
          $('.timeDate'+clickedOptionDay).addClass('active');

          $('.calendar_list').fadeIn();
          $('.active').show();
          }
       });


      //日報承認時のバリデーションチェック
      $('#formApproval').submit(function() {
          //入力されたテキストを取得
          var textValue = $('#report-content').val();
          if(textValue==''){
             $('#error-message').text('コメントを記入してください');
             return false;
          }
      });

      //ホバーアクション
      $('.newtest').hover(
        function() {
         $(this).addClass('text-active');
        },
        function() {
         $(this).removeClass('text-active');
        }
       );

        //ホバー時にテキスト変更
          $('.report_approvalActive').hover(
              function() {
                //ホバーしているテキスト取得
                var hoverOption = $(this).attr('data-approval')
                 $(this).text('レビューを見る');
              },
              function() {
                 $(this).text($(this).attr('data-approval'));
              }
           );


      var inputs = $('.input');
      //読み込み時に「:checked」の疑似クラスを持っているinputの値を取得する
      var checked = inputs.filter(':checked').val();

      //インプット要素がクリックされたら
      inputs.on('click', function(){
          //クリックされたinputとcheckedを比較
          if($(this).val() === checked ) {
              //inputの「:checked」をfalse
              $(this).prop('checked', false);
              //checkedを初期化
              checked = '';
              $('.active').removeClass('active');
          }else{
              $(this).prop('checked', true);
              checked = $(this).val();
              //クリック時に表示変更
              if($(this).val()==2){
                $('.month').addClass('active');
                 $('.date').removeClass('active');
                 $('.active').show();
              }
              if($(this).val()==3){
                $('.date').addClass('active');
                 $('.month').removeClass('active');
                 $('.active').show();
              }
              if($(this).val()==1){
                 $('.date').removeClass('active');
                 $('.month').removeClass('active');
              }
          }
      });

});
