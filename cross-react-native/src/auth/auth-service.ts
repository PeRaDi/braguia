import AsyncStorage from '@react-native-async-storage/async-storage';
import axiosInstance from '@src/network/axios.config.ts';
import {User} from '@src/auth/model.ts';
import {login, logout, store} from "@store/store.ts";
import {apiURL} from 'app.json';

export class AuthService {
  async login(username: string, password: string): Promise<User> {
      try {
        const response = await axiosInstance.post(`${apiURL}/login`, {
          username,
          password,
        });

        if (response.status === 200) {
          const cookies = response.headers['set-cookie'];
          if (cookies) {
            await AsyncStorage.setItem('cookie', cookies.join('; '));
            // Extract CSRF token from cookies
            const csrfToken = cookies.find(cookie => cookie.startsWith('csrftoken='));
            if (csrfToken) {
              await AsyncStorage.setItem('csrftoken', csrfToken.split('=')[1].split(';')[0]);
            }
          }
          const user = await this.getUserDetails();
          store.dispatch(login(user));
          return user;
        } else {
          throw new Error(`Erro de autenticação: ${response.statusText}`);
        }
      } catch (error) {
        console.error(error);
        if (error.response) {
          throw new Error(
            error.response.data.message ||
              'Erro de autenticação. Tenta novamente',
          );
        } else {
          throw error;
        }
      }
    }

  async logout(): Promise<void> {
      try {
        const response = await axiosInstance.post(`${apiURL}/logout`);
      } catch (error) {
        console.error('Erro ao fazer logout:', error);
      }
      store.dispatch(logout());
      await AsyncStorage.removeItem('cookie');
      await AsyncStorage.removeItem('csrftoken');
      await AsyncStorage.removeItem('user');
    }

  private async getUserDetails(): Promise<User> {
    const response = await axiosInstance.get('/user');
    if (response.status === 200) {
      const user = response.data;
      await AsyncStorage.setItem('user', JSON.stringify(user));
      return user;
    } else {
      throw new Error(`Erro detalhes do utilizador: ${response.statusText}`);
    }
  }
}

export default authServie = new AuthService();
