import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 100,
    duration: '30s',
};

export default function () {
    const loginPayload = JSON.stringify({
        email: 'test3@test.com',
        password: '123456789',
    });

    const loginRes = http.post('http://localhost:8080/api/auth/login', loginPayload, {
        headers: { 'Content-Type': 'application/json' },
    });

    check(loginRes, {
        'login status is 200': (r) => r.status === 200,
    });

    sleep(1);
}