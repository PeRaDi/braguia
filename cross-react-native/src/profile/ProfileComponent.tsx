import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, ActivityIndicator, ScrollView } from 'react-native';
import { Card } from 'react-native-paper';
import {apiURL} from 'app.json';
import {User} from '@auth/model.ts'

const UserProfile: React.FC = () => {
  const [profile, setProfile] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetch(`${apiURL}/user`)
      .then(response => {
        if (response.ok) {
          return response.json() as Promise<User>;
        }
        throw new Error('Network response was not ok');
      })
      .then(data => {
        setProfile(data);
        setError(null);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error fetching the user profile:', error);
        setError(error.message);
        setProfile(null);
        setLoading(false);
      });
  }, []);

  if (loading) {
    return (
      <View style={styles.loadingContainer}>
        <ActivityIndicator size="large" color="#0000ff" />
      </View>
    );
  }

  if (error) {
    return (
      <View style={styles.loadingContainer}>
        <Text style={styles.errorText}>Error: {error}</Text>
      </View>
    );
  }

  if (!profile) {
    return (
      <View style={styles.loadingContainer}>
        <Text style={styles.errorText}>No profile data</Text>
      </View>
    );
  }

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text style={styles.title}>User Profile</Text>
      <Card style={styles.card}>
        <Text style={styles.label}>Username:</Text>
        <Text style={styles.value}>{profile.username}</Text>
      </Card>
      <Card style={styles.card}>
        <Text style={styles.label}>First Name:</Text>
        <Text style={styles.value}>{profile.first_name}</Text>
      </Card>
      <Card style={styles.card}>
        <Text style={styles.label}>Last Name:</Text>
        <Text style={styles.value}>{profile.last_name}</Text>
      </Card>
      <Card style={styles.card}>
        <Text style={styles.label}>Email:</Text>
        <Text style={styles.value}>{profile.email}</Text>
      </Card>
      <Card style={styles.card}>
        <Text style={styles.label}>User Type:</Text>
        <Text style={styles.value}>{profile.user_type}</Text>
      </Card>
      <Card style={styles.card}>
        <Text style={styles.label}>Last Login:</Text>
        <Text style={styles.value}>{new Date(profile.last_login).toLocaleString()}</Text>
      </Card>
      <Card style={styles.card}>
        <Text style={styles.label}>Active:</Text>
        <Text style={styles.value}>{profile.is_active ? 'Yes' : 'No'}</Text>
      </Card>
      <Card style={styles.card}>
        <Text style={styles.label}>Staff:</Text>
        <Text style={styles.value}>{profile.is_staff ? 'Yes' : 'No'}</Text>
      </Card>
      <Card style={styles.card}>
        <Text style={styles.label}>Superuser:</Text>
        <Text style={styles.value}>{profile.is_superuser ? 'Yes' : 'No'}</Text>
      </Card>
      <Card style={styles.card}>
        <Text style={styles.label}>Date Joined:</Text>
        <Text style={styles.value}>{new Date(profile.date_joined).toLocaleString()}</Text>
      </Card>
      <Card style={styles.card}>
        <Text style={styles.label}>Groups:</Text>
        <Text style={styles.value}>{profile.groups.join(', ')}</Text>
      </Card>
      <Card style={styles.card}>
        <Text style={styles.label}>User Permissions:</Text>
        <Text style={styles.value}>{profile.user_permissions.join(', ')}</Text>
      </Card>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 20,
    backgroundColor: '#f5f5f5',
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
    textAlign: 'center',
  },
  card: {
    padding: 10,
    marginVertical: 5,
  },
  label: {
    fontWeight: 'bold',
    fontSize: 16,
  },
  value: {
    fontSize: 16,
  },
  errorText: {
    color: 'red',
    fontSize: 18,
  },
});

export default UserProfile;
