server {
    listen {{ .interface }}:{{ .port }} default_server;

    include /etc/nginx/includes/server_params.conf;
    include /etc/nginx/includes/proxy_params.conf;

    location / {
        allow   172.30.32.2;
        deny    all;

        proxy_pass http://backend;

        # Rewrite absolute asset/API paths for HA ingress.
        # The admin UI uses absolute paths (/assets/...) which the browser
        # resolves against the HA host instead of the ingress path.
        sub_filter_types text/html text/css application/javascript application/json;
        sub_filter_once off;

        # HTML attributes
        sub_filter 'href="/' 'href="{{ .ingress_entry }}/';
        sub_filter 'src="/' 'src="{{ .ingress_entry }}/';
        sub_filter 'action="/' 'action="{{ .ingress_entry }}/';

        # JavaScript asset references (dynamic imports, CSS preloads)
        sub_filter '"/assets/' '"{{ .ingress_entry }}/assets/';
        sub_filter "'/assets/" "'{{ .ingress_entry }}/assets/";

        # JavaScript API references
        sub_filter '"/api/' '"{{ .ingress_entry }}/api/';
        sub_filter "'/api/" "'{{ .ingress_entry }}/api/";
    }
}
