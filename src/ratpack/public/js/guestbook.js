var TIMEOUT;
        // just so we can't dev on our laptops with Safari
        var isiPad = navigator.userAgent.match(/iPad/i) != null;

        var bindEvent = (isiPad) ? 'touchstart' : 'click';
        function splash() {
            if (!$('#welcome').is(':visible')) {
                $('#cancel a').trigger(bindEvent);
            }
        }

        function resetTimeout() {
            clearTimeout(TIMEOUT);
            TIMEOUT = setTimeout(splash, 60 * 1000);
        }

        function show(section, e) {
            resetTimeout();
            $("#guest_table a.delete").hide();
            $('#welcome').hide();
            $('#entry').hide();
            $('#guests').hide();
            $(section).show()
            e.preventDefault();

        }

        $(document).ready(function () {
            resetTimeout();
            setTimeout(function () {
                window.scrollTo(0, 1)
            }, 100);

            var
                    welcome = $('#welcome'),
                    entry = $('#entry'),
                    guests = $('#guests'),
                    checkout_btn = $('#checkout_button');


            $('#checkout_button').bind(bindEvent, function (e) {
                show(guests, e);
                checkout_btn.hide();
            })

            $('#welcome').bind(bindEvent, function (e) {
                show(entry, e);
            })
            $('#cancel a').bind(bindEvent, function (e) {
                entry.find('input').val('');
                entry.find('select').val('1');
                show(welcome, e);
                checkout_btn.show();
            })
            $('#submit').bind(bindEvent, function (e) {
                show(guests, e);
                entry.find('input').blur();
                checkout_btn.hide();
                window.scrollTo(0, 1);
                $.ajax({
                    url: "/checkin",
                    data: $("#signin_form").serialize(),
                    success: function (data) {
                        $('#guest_table').prepend(data);
                        $.ajax({
                            url: "/send_email",
                            success: function (data) {
                                console.log(data);
                            }
                        });
                    }
                });
            })
            $('#additional').bind(bindEvent, function (e) {
                entry.find('input').val('');
                entry.find('select').val('1');
                show(welcome, e);
                checkout_btn.show();
            })


            $('td .checkout').live(bindEvent, function (e) {
                resetTimeout();
                e.preventDefault();
                var id = $(this).attr('id');
                $.ajax({
                    url: "/checkout",
                    data: {'id': id},
                    success: function (data) {
                        $('#' + id).parent().html('<time>' + data + '</time>');
                    }
                });
            });

            $('td .delete').live(bindEvent, function (e) {
                resetTimeout();
                e.preventDefault();
                var id = $(this).attr('id').substring(7);
                $.ajax({
                    url: "delete.php",
                    data: {'id': id},
                    success: function (data) {
                        $('#delete_' + id).parents('tr').fadeOut('fast', function () {
                            $(this).remove();
                        });
                    }
                });
            });

            $("#manage a").live(bindEvent, function (e) {
                var pass = prompt("Password");
                if (pass == 'gobloom') {
                    $("#guest_table a.delete").show();
                }
            });

            function preload(arrayOfImages) {
                $(arrayOfImages).each(function () {
                    $('<img/>')[0].src = this;
                });
            }

            preload([
                '/images/guests.png',
                '/images/signin.png'
            ]);

        });