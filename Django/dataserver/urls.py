from django.conf.urls import url
from dataserver import views

urlpatterns = [
    url(r'^list/$', views.location_all),
    url(r'^neighbours/(?P<latitude>[-+]?[0-9]*\.?[0-9]+)/(?P<longitude>[-+]?[0-9]*\.?[0-9]+)/$', views.location_by_coords),
    url(r'^nearby/(?P<latitude>[-+]?[0-9]*\.?[0-9]+)/(?P<longitude>[-+]?[0-9]*\.?[0-9]+)/$', views.location_by_coords_spark),
    url(r'^nearby/(?P<latitude>[-+]?[0-9]*\.?[0-9]+)/(?P<longitude>[-+]?[0-9]*\.?[0-9]+)/(?P<prefstring>[A-Za-z, ]+)/$', views.location_by_coords_pref_spark),
]
