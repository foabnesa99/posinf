import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IIzlaznaFaktura, defaultValue } from 'app/shared/model/izlazna-faktura.model';

const initialState: EntityState<IIzlaznaFaktura> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const apiUrl = 'api/izlazna-fakturas';

// Actions

export const getEntities = createAsyncThunk('izlaznaFaktura/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?cacheBuster=${new Date().getTime()}`;
  return axios.get<IIzlaznaFaktura[]>(requestUrl);
});

export const exportPdf = createAsyncThunk(
  'izlaznaFaktura/generatePdf',
 async (id: string | number) => {
  const requestUrl = `${apiUrl}/${id}/export`;
  return axios.get(requestUrl);
 }
);

export const getEntity = createAsyncThunk(
  'izlaznaFaktura/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IIzlaznaFaktura>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'izlaznaFaktura/create_entity',
  async (entity: IIzlaznaFaktura, thunkAPI) => {
    const result = await axios.post<IIzlaznaFaktura>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'izlaznaFaktura/update_entity',
  async (entity: IIzlaznaFaktura, thunkAPI) => {
    const result = await axios.put<IIzlaznaFaktura>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'izlaznaFaktura/partial_update_entity',
  async (entity: IIzlaznaFaktura, thunkAPI) => {
    const result = await axios.patch<IIzlaznaFaktura>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'izlaznaFaktura/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IIzlaznaFaktura>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const IzlaznaFakturaSlice = createEntitySlice({
  name: 'izlaznaFaktura',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = IzlaznaFakturaSlice.actions;

// Reducer
export default IzlaznaFakturaSlice.reducer;
