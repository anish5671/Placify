/** @type {import('tailwindcss').Config} */
const burgundyScale = {
  50: '#F7E9ED',
  100: '#F3DDE3',
  200: '#E9BBC6',
  300: '#761F35',
  400: '#A43D50',
  500: '#A43D50',
  600: '#651C2D',
  700: '#761F35',
  800: '#F7E9ED',
  900: '#F7E9ED',
  950: '#F2F3F5',
};

const successScale = {
  50: '#EAF4EF',
  100: '#D8EDE2',
  200: '#A8D4BB',
  300: '#25845A',
  400: '#25845A',
  500: '#25845A',
  600: '#1E704C',
  700: '#195D40',
  800: '#EAF4EF',
  900: '#EAF4EF',
  950: '#F2F3F5',
};

const warningScale = {
  50: '#FFF7E8',
  100: '#FCECCF',
  200: '#F3D69B',
  300: '#8C611B',
  400: '#D99A32',
  500: '#D99A32',
  600: '#B97C1D',
  700: '#8C611B',
  800: '#FFF7E8',
  900: '#FFF7E8',
  950: '#F2F3F5',
};

export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        slate: {
          50: '#F2F3F5',
          100: '#F2F3F5',
          200: '#E8E9EC',
          300: '#DEDFE3',
          400: '#73717A',
          500: '#73717A',
          600: '#5E5C63',
          700: '#DEDFE3',
          800: '#DEDFE3',
          900: '#FFFFFF',
          950: '#F2F3F5',
        },
        brand: burgundyScale,
        indigo: burgundyScale,
        violet: burgundyScale,
        purple: burgundyScale,
        blue: burgundyScale,
        cyan: burgundyScale,
        emerald: successScale,
        green: successScale,
        amber: warningScale,
        rose: burgundyScale,
        red: burgundyScale,
      },
    },
  },
  plugins: [],
}
