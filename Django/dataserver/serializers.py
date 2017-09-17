from rest_framework import serializers
from dataserver.models import Location

class LocationSerializer(serializers.ModelSerializer):
    class Meta:
        model = Location
        fields = ('name', 'url', 'phone', 'address', 'latitude', 'longitude', 'city', 'pin_code')
