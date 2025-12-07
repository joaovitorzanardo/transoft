INSERT INTO public.school VALUES ('768b3283-f690-45c1-acde-5b92ed221b98', '99700-000', 'Erechim', null, -27.613019683574628, -52.22965475767118, 'RS-331', 0, 'RS-331', 'RS', 'URI Erechim - Campus II');
INSERT INTO public.school VALUES ('5b4f2f8b-1e60-4843-aa75-5c9cf2dcc21b', '99709-910', 'Erechim', null, -27.648209905167086, -52.267694574349335, 'Fátima', 1621, 'Av. Sete de Setembro', 'RS', 'URI Erechim - Campus I');

INSERT INTO public.automaker VALUES ('31c3ca0f-2a99-4fcf-a3cf-7b5fee3fde77', 'Mercedes-Benz');
INSERT INTO public.automaker VALUES ('7046fba1-2ae1-4ad5-89e7-7331657b7c3a', 'Renault');

INSERT INTO public.vehicle_model VALUES ('321b1227-dc2f-40ca-a2fb-1a183032ecd9', 'Sprinter', 2023, '31c3ca0f-2a99-4fcf-a3cf-7b5fee3fde77');
INSERT INTO public.vehicle_model VALUES ('cd5e99fd-f1d0-4f79-bbe4-b5c8562891dd', 'Master Minibus', 2024, '7046fba1-2ae1-4ad5-89e7-7331657b7c3a');

INSERT INTO public.company VALUES ('4cf349ba-c74d-4ba2-a946-04e5a8398191','12.345.678/9012-34','joaovizan@hotmail.com','Zanardo Transporte Ltda.');

INSERT INTO public.user_account VALUES ('c1848040-f796-4e58-942c-22943e8fa182',true,'joaovizan@hotmail.com',true,'Zanardo Transporte Ltda.','$2a$10$iN6dJqjL60M9TKHvCGuNje4TmyU/BMrCJSVF2vHR2ieONm4KBLJm6','MANAGER','4cf349ba-c74d-4ba2-a946-04e5a8398191');
INSERT INTO public.user_account VALUES ('c5feae0a-c7d7-4bd5-9eb7-a54c183b7911',false,'031883@aluno.uricer.edu.br',true,'Antônio Lopes','$2a$10$0MUxtijxQToaNKjFRHWizeFMmC01SZtVR2yyVm5A7xCYJZJKGPo8K','DRIVER','4cf349ba-c74d-4ba2-a946-04e5a8398191');
INSERT INTO public.user_account VALUES ('9e2d24d0-83cf-4199-beb9-22ff687152a8',false,'joviz2411@gmail.com',true,'Diogo Moreira','$2a$10$H4J9VQFF7WBi5LW3IZLi1.ebxuPPoqjBB9vQG6WUbcJxowW42oINy','PASSENGER','4cf349ba-c74d-4ba2-a946-04e5a8398191');
INSERT INTO public.user_account VALUES ('cc69ee3d-6ea5-46c4-a5c4-edc5af0d1ff8',false,'joaovitorzanardoorg@gmail.com',true,'Fernando Rodrigues','$2a$10$sfk2V2XlPWRKBWRxqFM20eyD5yXu2mmWeK1mebRi2wmChX8tE9lvu','PASSENGER','4cf349ba-c74d-4ba2-a946-04e5a8398191');

INSERT INTO public.vehicle VALUES ('a23bc0b0-399b-43d5-808b-430ff676e9f4',true,15,'GOZ-5T93','4cf349ba-c74d-4ba2-a946-04e5a8398191','321b1227-dc2f-40ca-a2fb-1a183032ecd9');
INSERT INTO public.vehicle VALUES ('ebff189c-1f6b-45a8-8d5f-6034a9b0949e',true,20,'EFD-3V16','4cf349ba-c74d-4ba2-a946-04e5a8398191','cd5e99fd-f1d0-4f79-bbe4-b5c8562891dd');

INSERT INTO public.driver VALUES ('64df1720-142c-48af-8c3c-cdaebf882f38','2029-12-06','33122611988','031883@aluno.uricer.edu.br','Antônio Lopes','54','95331-4518','4cf349ba-c74d-4ba2-a946-04e5a8398191','c5feae0a-c7d7-4bd5-9eb7-a54c183b7911');

INSERT INTO public.route VALUES ('b14b22f1-b923-4271-932b-dd5caca5cc6d',true,true,true,true,true,true,'19:15:00','18:00:00','Rota URI - Campus II - NOTURNO','23:15:00','22:25:00','4cf349ba-c74d-4ba2-a946-04e5a8398191','64df1720-142c-48af-8c3c-cdaebf882f38','a23bc0b0-399b-43d5-808b-430ff676e9f4','768b3283-f690-45c1-acde-5b92ed221b98');
INSERT INTO public.route VALUES ('573b277f-6296-45d6-b444-6536a8e36546',true,true,true,true,true,true,'19:15:00','18:00:00','Rota URI - Campus I - NOTURNO','23:15:00','22:25:00','4cf349ba-c74d-4ba2-a946-04e5a8398191','64df1720-142c-48af-8c3c-cdaebf882f38','ebff189c-1f6b-45a8-8d5f-6034a9b0949e','5b4f2f8b-1e60-4843-aa75-5c9cf2dcc21b');

INSERT INTO public.passenger VALUES ('81fb0be0-b4db-48d7-8c06-d238737dcbfe','99700-036','Erechim','',-27.6294884,-52.2775657,'Centro',101,'Avenida Germano Hoffmann','RS','joviz2411@gmail.com','Diogo Moreira','54','95434-8484','4cf349ba-c74d-4ba2-a946-04e5a8398191','b14b22f1-b923-4271-932b-dd5caca5cc6d','9e2d24d0-83cf-4199-beb9-22ff687152a8');
INSERT INTO public.passenger VALUES ('da14e53e-6cac-4cfd-9263-e3069334c900','99700-066','Erechim','',-27.6341527,-52.27669539999999,'Centro',216,'Rua Itália','RS','joaovitorzanardoorg@gmail.com','Fernando Rodrigues','54','95439-7401','4cf349ba-c74d-4ba2-a946-04e5a8398191','b14b22f1-b923-4271-932b-dd5caca5cc6d','cc69ee3d-6ea5-46c4-a5c4-edc5af0d1ff8');