server {
    listen {{ .interface }}:{{ .port }} default_server;

    include /etc/nginx/includes/server_params.conf;
    include /etc/nginx/includes/proxy_params.conf;

    location / {
        allow   172.30.32.2;
        deny    all;

        proxy_pass http://backend;

        # Rewrite absolute paths so assets and API calls go through
        # the HA ingress prefix instead of the HA root.
        sub_filter_types text/html text/css application/javascript;
        sub_filter_once off;
        sub_filter 'href="/' 'href="{{ .ingress_entry }}/';
        sub_filter 'src="/' 'src="{{ .ingress_entry }}/';
        sub_filter 'action="/' 'action="{{ .ingress_entry }}/';
        sub_filter '"/api/' '"{{ .ingress_entry }}/api/';
        sub_filter '"/assets/' '"{{ .ingress_entry }}/assets/';
    }
}
